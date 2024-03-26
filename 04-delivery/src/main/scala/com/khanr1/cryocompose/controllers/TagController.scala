package com.khanr1
package cryocompose
package controllers

import services.TagService

import scala.util.chaining.*

import org.http4s.*
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

import response.Tag.given

import cats.*
import cats.syntax.all.*
import cats.data.*

import io.circe.*
import io.circe.syntax.*
import io.circe.generic.semiauto.*

import io.github.iltotore.iron.circe.given

object TagController:
  def make[F[_]: effect.Async, TagID](tagServices: TagService[F, TagID]): Controller[F] =
    new Controller[F] with Http4sDsl[F]:
      private val prefixPath: String = "/tags"

      private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
        case GET -> Root => searchAllTag
      }

      val routes: HttpRoutes[F] = Router(
        prefixPath -> httpRoutes
      )

      def searchAllTag: F[Response[F]] = tagServices
        .findAllTag
        .flatMap { tags =>
          tags
            .map(response.Tag(_))
            .asJson
            .pipe(Ok(_))
        }
