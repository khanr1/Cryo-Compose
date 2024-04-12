package com.khanr1
package cryocompose
package controllers
package inMemory

import scala.util.chaining.*

import com.khanr1.cryocompose.services.CategoryService
import com.khanr1.cryocompose.helpers.Parse
import org.http4s.*
import org.http4s.circe.*

import cryocompose.request.Category.{ *, given }

import cats.*
import io.circe.syntax.*
import io.github.iltotore.iron.*
import io.circe.DecodingFailure

object CategoryController:
  def make[F[_]: effect.Async, CategoryID](
    categoryService: CategoryService[F, CategoryID]
  )(using
    parse: helpers.Parse[String, CategoryID]
  ): Controller[F] =
    new Controller[F] with Http4sDsl[F]:
      private val prefixPath = "/categories"
      private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
        // GET
        case GET -> Root => searchAllCategories
        case GET -> Root / "roots" => searchRoots
        case GET -> Root / "children" / id => searchChildren(id)
        case GET -> Root / id => searchById(id)
        // DELETE
        case DELETE -> Root / id => delete(id)
        // POST
        case r @ POST -> Root =>
          r.as[cryocompose.request.Category.Create[CategoryID]]
            .flatMap(x => create(x))
            .handleErrorWith(e => displayDecodeError(e))
        // PUT
        case r @ PUT -> Root / id =>
          r.as[cryocompose.request.Category.Update[CategoryID]]
            .flatMap(update(id))
            .handleErrorWith(e => displayDecodeError(e))
      }
      override def routes: HttpRoutes[F] = Router(
        prefixPath -> httpRoutes
      )
      /** Search all the categories
        *
        * @return
        */
      private def searchAllCategories =
        categoryService
          .findAll
          .flatMap(categories =>
            categories
              .map(response.Category.create(_))
              .asJson
              .pipe(Ok(_))
          )
        /** Search for all the roots categories. Root categories are categories that have no parent
          *
          * @return
          */
      private def searchRoots =
        categoryService
          .findRoot
          .flatMap(categories =>
            categories
              .map(response.Category.create(_))
              .asJson
              .pipe(Ok(_))
          )
      private def searchChildren(id: String): F[Response[F]] =
        parse(id).fold(
          t => BadGateway(t.getMessage()),
          id =>
            categoryService
              .findChildren(id)
              .flatMap(categories =>
                categories
                  .map(response.Category.create(_))
                  .asJson
                  .pipe(Ok(_))
              ),
        )
      private def searchById(id: String) =
        parse(id).fold(
          t => BadRequest(t.getMessage()),
          id =>
            categoryService
              .findByID(id)
              .flatMap(maybeCategory =>
                maybeCategory
                  .map(response.Category.create(_))
                  .fold(
                    NotFound(s"Not cateogry with ID ${id}")
                  )(
                    _.asJson
                      .pipe(Ok(_))
                  )
              ),
        )
      private def delete(id: String) = parse(id).fold(
        t => BadRequest(t.getMessage()),
        id =>
          categoryService
            .deleteCatgory(id)
            .flatMap(x => NoContent())
            .handleErrorWith(e => BadRequest(e.getMessage())),
      )
      private def displayDecodeError(e: Throwable): F[Response[F]] =
        e.getCause() match
          case d: DecodingFailure => BadRequest(d.message + "decoding failed")
          case _ => BadRequest(e.getMessage())
      private def create(input: cryocompose.request.Category.Create[CategoryID]): F[Response[F]] =
        categoryService
          .createCategory(
            CategoryParam(input.name, input.description, input.parent)
          )
          .map(response.Category.create[CategoryID](_))
          .map(_.asJson)
          .flatMap(Created(_))
          .handleErrorWith(e => Ok(e.getMessage()))
      private def update(id: String)
        : cryocompose.request.Category.Update[CategoryID] => F[Response[F]] =
        _.fold(
          updateName(id),
          updateDescription(id),
          updateParent(id),
          updateAll(id),
        )
      def updateName(id: String)(name: CategoryName) =
        parseAndCheck(id)(cat =>
          categoryService
            .updateCategory(cat.copy(name = name))
            .flatMap(x => Ok())
        )
      def updateParent(id: String)(p: Option[CategoryID]) = parseAndCheck(id)(cat =>
        categoryService
          .updateCategory(cat.copy(parent = p))
          .flatMap(x => Ok())
      )
      def updateDescription(id: String)(d: CategoryDescription) =
        parseAndCheck(id)(cat =>
          categoryService
            .updateCategory(cat.copy(description = d))
            .flatMap(x => Ok())
        )
      def updateAll(
        id: String
      )(
        n: CategoryName,
        d: CategoryDescription,
        p: Option[CategoryID],
      ) = parseAndCheck(id)(cat =>
        categoryService
          .updateCategory(cat.copy(name = n, description = d, parent = p))
          .flatMap(x => Ok())
      )

      /*Helper to parse input string to ID of type categoryID. when the parsing
      fails, the parsing fails, the helper return a response NotAcceptable, is
      the parsing is successfull, a function taking the id parsed to a
      CategoryID and returning aresponse is applied */
      private def parseID(
        id: String
      )(
        success: CategoryID => F[Response[F]]
      ): F[Response[F]] =
        parse(id).fold(
          throwable => NotAcceptable(" the given ID cannot be parsed"),
          id => success(id),
        )

      /* Helper to check if given  category with ID CategoryID exist . If the
      category  does not exist it returns a NotFound response otherwise applied
      the function function taking the found category and return a response */
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
      /* helper to parse and check an  ID. Simply the combination of both previous
      function */
      private def parseAndCheck(
        id: String
      )(
        success: Category[CategoryID] => F[Response[F]]
      ) =
        parseID(id)(checkID(success))
