package com.khanr1
package cryocompose

import cats.effect.*
import cats.implicits.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.*
import org.http4s.server.Server
import org.http4s.server.defaults.Banner
import com.comcast.ip4s.*
import org.typelevel.log4cats.Logger

trait HttpServer[F[_]]:
  def serve: Resource[F, Server]

// A server serves an HTTPApp
object HttpServer:
  def make[F[_]: Async: Logger](app: HttpApp[F]): HttpServer[F] =
    new HttpServer:
      val serve: Resource[F, Server] =
        EmberServerBuilder
          .default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(app)
          .build
          .evalTap(showEmberBanner)
      private def showEmberBanner(s: Server): F[Unit] =
        Logger[F].info(
          s"\n${Banner.mkString("\n")}\nHTTP Server started at ${s.address}"
        )
