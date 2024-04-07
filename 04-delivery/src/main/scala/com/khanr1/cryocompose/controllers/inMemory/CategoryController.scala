package com.khanr1
package cryocompose
package controllers
package inMemory

import scala.util.chaining.*

import com.khanr1.cryocompose.services.CategoryService
import com.khanr1.cryocompose.helpers.Parse
import org.http4s.*
import org.http4s.circe.*

import cats.*
import io.circe.syntax.*
import io.github.iltotore.iron.constraint.any.Not

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
      }
      override def routes: HttpRoutes[F] = Router(
        prefixPath -> httpRoutes
      )

      private def searchAllCategories =
        categoryService
          .findAll
          .flatMap(categories =>
            categories
              .map(response.Category.create(_))
              .asJson
              .pipe(Ok(_))
          )
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
