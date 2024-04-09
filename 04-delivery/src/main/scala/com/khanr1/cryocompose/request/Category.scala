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

object Category:
  case class Create[CategoryID](
    name: CategoryName,
    description: CategoryDescription,
    parent: Option[CategoryID],
  )
  object Create:
    def create[CategoryID](
      name: String,
      description: String,
      parent: String,
    )(using
      parse: Parse[String, Option[CategoryID]]
    ): ValidatedNel[String, Create[CategoryID]] =
      (
        CategoryName.validatedNel(name),
        CategoryDescription.validatedNel(description),
        parse(parent).leftMap(_.toString()).toValidatedNel,
      ).mapN(Create(_, _, _))

    given decoder[CategoryID](
      using
      parse: Parse[String, CategoryID]
    ): Decoder[Create[CategoryID]] =
      Decoder
        .forProduct3("name", "description", "parent")(create[CategoryID](_, _, _))
        .emap(
          _.toEither
            .leftMap(e => e.mkString_("\n"))
        )

    given entityDecoder[F[_]: effect.Concurrent, CategoryID](
      using
      parse: Parse[String, CategoryID]
    ): EntityDecoder[F, Create[CategoryID]] =
      jsonOf
