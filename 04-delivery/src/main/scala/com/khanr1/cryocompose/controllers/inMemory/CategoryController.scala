package com.khanr1
package cryocompose
package controllers
package inMemory

import cats.*
import com.khanr1.cryocompose.helpers.Parse
import com.khanr1.cryocompose.services.CategoryService
import io.circe.Decoder
import io.circe.DecodingFailure
import io.circe.Encoder
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*

import scala.util.chaining.*

/** An object responsible for creating an HTTP controller for category-related operations.
  */
object CategoryController:
  given entityDecoder[F[_]: effect.Concurrent, CategoryID](
    using
    d: Decoder[Option[CategoryID]]
  ): EntityDecoder[F, CategoryParam[CategoryID]] =
    jsonOf
  /** Creates a new instance of the HTTP controller for category operations.
    *
    * @tparam F the effect type, typically a type constructor representing a monadic effect such as `IO`, `Future`, etc.
    * @tparam CategoryID the type of the identifier for categories.
    * @param categoryService the service responsible for handling category-related operations.
    * @param parse the parser for converting strings to category identifiers.
    * @return an instance of the HTTP controller for categories.
    */
  def make[F[_]: effect.Async, CategoryID: Encoder: Decoder](
    categoryService: CategoryService[F, CategoryID]
  )(using
    parse: helpers.Parse[String, CategoryID]
  ): Controller[F] =
    /** An instance of the HTTP controller for categories.
      */
    new Controller[F] with Http4sDsl[F]:

      // Define private variables
      private val prefixPath = "/categories"
      private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {

        // Define HTTP routes
        case GET -> Root => searchAllCategories
        case GET -> Root / "roots" => searchRoots
        case GET -> Root / "children" / id => searchChildren(id)
        case GET -> Root / id => searchById(id)
        case DELETE -> Root / id => delete(id)
        case r @ POST -> Root => handlePostRequest(r)
        case r @ PUT -> Root / id => handlePutRequest(r, id)
      }

      /** Handles POST requests.
        *
        * @param request the incoming POST request.
        * @return the HTTP response for the POST request.
        */
      private def handlePostRequest(
        request: Request[F]
      ): F[Response[F]] =
        request
          .as[CategoryParam[CategoryID]]
          .flatMap(create)
          .handleErrorWith(displayDecodeError)

      /** Handles PUT requests.
        *
        * @param request the incoming PUT request.
        * @param id the identifier of the category to be updated.
        * @return the HTTP response for the PUT request.
        */
      private def handlePutRequest(
        request: Request[F],
        id: String,
      ): F[Response[F]] =
        request
          .as[CategoryParam[CategoryID]]
          .flatMap(update(id))
          .handleErrorWith(displayDecodeError)

      /** Retrieves the HTTP routes for the category controller.
        *
        * @return the HTTP routes for the category controller.
        */
      override def routes: HttpRoutes[F] =
        Router(prefixPath -> httpRoutes)

      /** Searches for all categories and returns them as JSON.
        *
        * @return an effectful computation yielding the HTTP response containing all categories as JSON.
        */
      private def searchAllCategories =
        categoryService
          .findAll
          .flatMap(categories =>
            categories
              .asJson
              .pipe(Ok(_))
          )
      /** Searches for root categories and returns them as JSON.
        *
        * @return an effectful computation yielding the HTTP response containing root categories as JSON.
        */
      private def searchRoots =
        categoryService
          .findRoot
          .flatMap(categories =>
            categories
              .asJson
              .pipe(Ok(_))
          )
      /** Searches for children categories of a given category ID and returns them as JSON.
        *
        * @param id the identifier of the parent category.
        * @return an effectful computation yielding the HTTP response containing children categories as JSON.
        */
      private def searchChildren(id: String): F[Response[F]] =
        parse(id).fold(
          t => BadGateway(t.getMessage()),
          id =>
            categoryService
              .findChildren(id)
              .flatMap(categories =>
                categories
                  .asJson
                  .pipe(Ok(_))
              ),
        )
      /** Searches for a category by its ID and returns it as JSON.
        *
        * @param id the identifier of the category to search for.
        * @return an effectful computation yielding the HTTP response containing the category as JSON.
        */
      private def searchById(id: String) =
        parse(id).fold(
          t => BadRequest(t.getMessage()),
          id =>
            categoryService
              .findByID(id)
              .flatMap(maybeCategory =>
                maybeCategory
                  .fold(
                    NotFound(s"Not cateogry with ID ${id}")
                  )(
                    _.asJson
                      .pipe(Ok(_))
                  )
              ),
        )
      /** Deletes a category by its ID.
        *
        * @param id the identifier of the category to delete.
        * @return an effectful computation representing the deletion operation.
        */
      private def delete(id: String) = parse(id).fold(
        t => BadRequest(t.getMessage()),
        id =>
          categoryService
            .deleteCatgory(id)
            .flatMap(x => NoContent())
            .handleErrorWith(e => BadRequest(e.getMessage())),
      )
      /** Handles decoding errors during request processing.
        *
        * @param e the throwable representing the decoding error.
        * @return an effectful computation yielding the appropriate HTTP response.
        */
      private def displayDecodeError(e: Throwable): F[Response[F]] =
        e.getCause() match
          case d: DecodingFailure => BadRequest(d.message + "decoding failed")
          case _ => BadRequest(e.getMessage())
      /** Creates a new category based on the provided input.
        *
        * @param input the input for creating the category.
        * @return an effectful computation yielding the HTTP response for the creation operation.
        */
      private def create(input: CategoryParam[CategoryID]): F[Response[F]] =
        categoryService
          .createCategory(
            input
          )
          .map(_.asJson)
          .flatMap(Created(_))
          .handleErrorWith(e => Ok(e.getMessage()))
      /** Updates a category based on the provided input.
        *
        * @param id the identifier of the category to update.
        * @return a function accepting the update input and yielding the HTTP response for the update operation.
        */
      private def update(id: String)(param: CategoryParam[CategoryID]) = parseAndCheck(id)(cat =>
        categoryService
          .updateCategory(
            cat.copy(name = param.name, description = param.description, parent = param.parent)
          )
          .flatMap(x => Ok())
      )

      /** Parses the input string to a category ID and applies the success function if successful.
        *
        * @param id the input string representing the category ID.
        * @param success the function to apply if parsing is successful.
        * @return an effectful computation representing the parsing and processing operation.
        */
      private def parseID(
        id: String
      )(
        success: CategoryID => F[Response[F]]
      ): F[Response[F]] =
        parse(id).fold(
          throwable => NotAcceptable(" the given ID cannot be parsed"),
          id => success(id),
        )

      /** Checks if the category with the given ID exists and applies the success function if it does.
        *
        * @param success the function to apply if the category exists.
        * @param id the identifier of the category to check.
        * @return an effectful computation representing the existence check and processing operation.
        */
      private def checkID(
        success: Category[CategoryID] => F[Response[F]]
      )(
        id: CategoryID
      ): F[Response[F]] =
        categoryService
          .findByID(id)
          .flatMap(
            _.fold(NotFound(s"No category with id $id found"))(cat => success(cat))
          )
      /** Parses the input string to a category ID, checks if the category exists, and applies the success function if it does.
        *
        * @param id the input string representing the category ID.
        * @param success the function to apply if parsing and checking are successful.
        * @return an effectful computation representing the parsing, checking, and processing operation.
        */
      private def parseAndCheck(
        id: String
      )(
        success: Category[CategoryID] => F[Response[F]]
      ) =
        parseID(id)(checkID(success))
