package com.khanr1.cryocompose.controllers

import cats.*
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import org.http4s.StaticFile
import org.http4s.server.Router

object MainController:
  def make[F[_]: effect.Sync]: Controller[F] = new Controller[F] with Http4sDsl[F]:
    private val prefixPath: String = "/cryocompose"

    private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
      case request @ GET -> Root =>
        StaticFile
          .fromResource[F]("index.html", Some(request))
          .getOrElseF(NotFound())
      case request @ GET -> Root / "frontend" =>
        StaticFile
          .fromResource[F]("frontend.js", Some(request))
          .getOrElseF(NotFound())
    }

    override val routes: HttpRoutes[F] = Router(
      prefixPath -> httpRoutes
    )
