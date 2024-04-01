package com.khanr1
package cryocompose
package controllers
package inMemory

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
  def make[F[_]: effect.Async, TagID](
    tagServices: TagService[F, TagID]
  )(using
    parse: helpers.Parse[String, TagID]
  ): Controller[F] =
    new Controller[F] with Http4sDsl[F]:
      private val prefixPath: String = "/tags"

      private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
        // GET ROUTES
        case GET -> Root => searchAllTag
        // POST ROUTES
        case r @ POST -> Root =>
          r.as[request.Tag.Create]
            .flatMap(x => create(x))
            .handleErrorWith(e => displayDecodeError(e))
        // DELETE ROUTES
        case DELETE -> Root / id => delete(id)

      }

      val routes: HttpRoutes[F] = Router(
        prefixPath -> httpRoutes
      )

      /** Helper to display the error when trying to decode a request
        *
        * @param e
        * @return
        */
      private def displayDecodeError(e: Throwable): F[Response[F]] =
        e.getCause() match
          case d: DecodingFailure => BadRequest(d.message)
          case _ => BadRequest(e.getMessage())
      /** Helper method to for creating a Tag from a POST request
        *
        * @param input
        * @return a http Response
        */
      private def create(input: request.Tag.Create): F[Response[F]] =
        tagServices
          .createTag(
            input.name
          )
          .map(response.Tag(_))
          .map(_.asJson)
          .flatMap(Created(_))
          .handleErrorWith(e => Ok(e.getMessage()))

      private def delete(id: String): F[Response[F]] =
        parseID(id)(
          tagServices
            .deleteTag(_)
            .flatMap(x => NoContent())
            .handleErrorWith(e => NotAcceptable(e.getMessage()))
        )

      /*Helper to parse input string to ID of type TagID. when the parsing
      fails, the parsing fails, the helper return a response NotAcceptable, is
      the parsing is successfull, a function taking the id parsed to a
      TagID and returning aresponse is applied */
      private def parseID(
        id: String
      )(
        success: TagID => F[Response[F]]
      ): F[Response[F]] =
        parse(id).fold(
          throwable => NotAcceptable(" the given ID cannot be parsed"),
          id => success(id),
        )

      /** helper method to search all tags
        */
      def searchAllTag: F[Response[F]] = tagServices
        .findAllTag
        .flatMap { tags =>
          tags
            .map(response.Tag(_))
            .asJson
            .pipe(Ok(_))
        }

object request:
  object Tag:
    case class Create(name: TagName)

    object Create:
      def create(name: String): ValidatedNel[String, Create] =
        TagName.either(name).map(Create(_)).toValidatedNel
      given Decoder[Create] = Decoder
        .forProduct1("name")(create(_))
        .emap(
          _.toEither.leftMap(x => x.mkString_("\n"))
        )
      given entityDecoder[F[_]: cats.effect.Concurrent]: EntityDecoder[F, Create] = jsonOf
      given Encoder[Create] = Encoder.forProduct1("name")(n => n.name)
      given entityEncoder[F[_]: cats.effect.Concurrent]: EntityEncoder[F, Create] = jsonEncoderOf
