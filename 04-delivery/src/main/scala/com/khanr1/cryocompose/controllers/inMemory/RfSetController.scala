package com.khanr1
package cryocompose
package controllers
package inMemory

import cats.*
import org.http4s.*
import org.http4s.circe.*
import scala.util.chaining.*
import com.khanr1.cryocompose.services.wiring.rf.RfSetService
import io.circe.syntax.*

object RfSetController:
  def make[
    F[_]: effect.Async,
    RfAssemblyID: Encoder: Decoder,
    RfConnectorID: Encoder: Decoder,
    CategoryID: Encoder: Decoder,
    TagID: Encoder: Decoder,
  ](
    rfService: RfSetService[F, RfAssemblyID, RfConnectorID, CategoryID, TagID]
  ): Controller[F] = new Controller[F] with Http4sDsl[F]:
    private val prefixPath = "/rf/rfSet"
    private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
      case GET -> Root => searchAllSet
    }

    val routes: HttpRoutes[F] = Router(
      prefixPath -> httpRoutes
    )

    def searchAllSet = rfService
      .findAllRfSet
      .flatMap(sets =>
        sets
          .asJson
          .pipe(Ok(_))
      )
