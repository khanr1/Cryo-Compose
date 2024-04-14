package com.khanr1
package cryocompose
package request

import cats.*
import cats.data.*
import cats.syntax.all.*
import com.khanr1.cryocompose.helpers.Parse
import io.circe.*
import io.circe.syntax.*
import io.github.iltotore.iron.cats.*
import org.http4s.*
import org.http4s.circe.*

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
