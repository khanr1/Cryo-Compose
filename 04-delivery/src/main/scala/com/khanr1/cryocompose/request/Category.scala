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

  enum Update[CategoryID]:
    case Name(name: CategoryName)
    case Description(description: CategoryDescription)
    case Parent(parent: Option[CategoryID])
    case All(
      name: CategoryName,
      description: CategoryDescription,
      parent: Option[CategoryID],
    )

    final def fold[B](
      ifName: CategoryName => B,
      ifDescription: CategoryDescription => B,
      ifParent: Option[CategoryID] => B,
      ifAll: (CategoryName, CategoryDescription, Option[CategoryID]) => B,
    ): B = this match
      case All(name, description, parent) => ifAll(name, description, parent)
      case Name(name) => ifName(name)
      case Description(description) => ifDescription(description)
      case Parent(parent) => ifParent(parent)

  object Update:
    def name[CategoryID](name: String): ValidatedNel[String, Update.Name[CategoryID]] =
      CategoryName.validatedNel(name).map(Update.Name(_))

    def description[CategoryID](description: String)
      : ValidatedNel[String, Update.Description[CategoryID]] =
      CategoryDescription.validatedNel(description).map(Update.Description(_))

    def parent[CategoryID](
      parent: String
    )(using
      parse: Parse[String, Option[CategoryID]]
    ): ValidatedNel[String, Update.Parent[CategoryID]] =
      parse(parent).leftMap(_.getMessage()).toValidatedNel.map(Update.Parent(_))
    def all[CategoryID](
      name: String,
      description: String,
      parent: String,
    )(using
      parse: Parse[String, Option[CategoryID]]
    ): ValidatedNel[String, Update.All[CategoryID]] =
      (
        CategoryName.validatedNel(name),
        CategoryDescription.validatedNel(description),
        parse(parent).leftMap(_.getMessage()).toValidatedNel,
      ).mapN(Update.All(_, _, _))

    given decoder[CategoryID](
      using
      parse: Parse[String, Option[CategoryID]]
    ): Decoder[Update[CategoryID]] = NonEmptyChain[Decoder[Update[CategoryID]]](
      Decoder
        .forProduct3("name", "description", "parent")(all(_, _, _))
        .emap(_.toEither.leftMap(_.mkString_("\n"))),
      Decoder
        .forProduct1("name")(name[CategoryID](_))
        .emap(_.toEither.leftMap(_.mkString_("\n")))
        .widen,
      Decoder
        .forProduct1("description")(description[CategoryID](_))
        .emap(_.toEither.leftMap(_.mkString_("\n")))
        .widen,
      Decoder
        .forProduct1("parent")(parent[CategoryID](_))
        .emap(_.toEither.leftMap(_.mkString_("\n")))
        .widen,
    ).reduceLeft(_ `or` _)

  given entityDecoder[F[_]: effect.Concurrent, CategoryID](
    using
    parse: Parse[String, Option[CategoryID]]
  ): EntityDecoder[F, Update[CategoryID]] =
    jsonOf
