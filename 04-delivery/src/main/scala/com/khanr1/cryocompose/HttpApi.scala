package com.khanr1
package cryocompose

import com.khanr1.cryocompose.controllers.Controller
import org.http4s.HttpApp
import cats.*
import org.http4s.server.middleware.RequestLogger
import org.http4s.server.middleware.ResponseLogger
import org.http4s.HttpRoutes

object HttpApi:
  def make[F[_]: effect.Async: Applicative](
    controllers: Controller[F]*
  ): org.http4s.HttpApp[F] =

    /* logger: This is a function that takes an HttpApp and returns another HttpApp
    with request and response logging applied using RequestLogger and ResponseLogger
    middleware.  Loggin request header and body and looging response hearder. */
    val logger: HttpApp[F] => HttpApp[F] = { (http: HttpApp[F]) =>
      RequestLogger.httpApp(true, true)(http)
    } andThen { (http: HttpApp[F]) =>
      ResponseLogger.httpApp(true, false)(http)
    }
    val bindedRoutes: HttpRoutes[F] =
      controllers
        .map(_.routes)
        .reduceLeft(_ <+> _)

    logger(bindedRoutes.orNotFound)
