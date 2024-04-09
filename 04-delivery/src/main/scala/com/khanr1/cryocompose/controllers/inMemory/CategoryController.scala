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

        case GET -> Root => searchAllCategories
        case GET -> Root / "roots" => searchRoots
        case GET -> Root / "children" / id => searchChildren(id)
        case GET -> Root / id => searchById(id)

        case DELETE -> Root / id => delete(id)

        case r @ POST -> Root =>
          r.as[cryocompose.request.Category.Create[CategoryID]]
            .flatMap(x => create(x))
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
