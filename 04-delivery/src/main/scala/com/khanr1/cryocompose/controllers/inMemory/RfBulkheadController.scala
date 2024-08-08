package com.khanr1
package cryocompose
package controllers
package inMemory

import services.wiring.rf.RfConnectorService
import wiring.rf.RfConnectorParam

import scala.util.chaining.*

import org.http4s.*
import org.http4s.circe.*

import cats.*
import cats.data.*

import io.circe.*
import io.circe.syntax.*
import com.khanr1.cryocompose.wiring.rf.RFmaterial
import com.khanr1.cryocompose.wiring.rf.RfBulkheadParam
import com.khanr1.cryocompose.services.wiring.rf.RfBulkheadService

object RfBulkheadController:
  given entityDecoder[
    F[_]: effect.Concurrent,
    RfConnectorID: Decoder,
    CategoryID: Decoder,
    TagID: Decoder,
  ]: EntityDecoder[F, RfBulkheadParam[RfConnectorID, CategoryID, TagID]] =
    jsonOf

  def make[
    F[_]: effect.Async,
    RfConnectorID: Encoder: Decoder,
    CategoryID: Encoder: Decoder,
    TagID: Encoder: Decoder,
  ](
    RfBulkheadService: RfBulkheadService[F, RfConnectorID, CategoryID, TagID]
  )(using
    parse: helpers.Parse[String, RfConnectorID]
  ): Controller[F] =
    new Controller[F] with Http4sDsl[F]:
      private val prefixPath: String = "/rf/rfbulkhead"

      private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
        // GET ROUTES
        case GET -> Root => searchAllTag
        // POST ROUTES
        case r @ POST -> Root =>
          r.as[RfBulkheadParam[RfConnectorID, CategoryID, TagID]]
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
      private def create(input: RfBulkheadParam[RfConnectorID, CategoryID, TagID]): F[Response[F]] =
        RfBulkheadService
          .create(
            input
          )
          .map(_.asJson)
          .flatMap(Created(_))
          .handleErrorWith(e => Ok(e.getMessage()))

      private def delete(id: String): F[Response[F]] =
        parseID(id)(
          RfBulkheadService
            .delete(_)
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
        success: RfConnectorID => F[Response[F]]
      ): F[Response[F]] =
        parse(id).fold(
          throwable => NotAcceptable(" the given ID cannot be parsed"),
          id => success(id),
        )

      /** helper method to search all tags
        */
      def searchAllTag: F[Response[F]] = RfBulkheadService
        .findAllRfBulkhead
        .flatMap { tags =>
          tags
            .asJson
            .pipe(Ok(_))
        }
