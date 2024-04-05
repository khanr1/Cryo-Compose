package com.khanr1.cryocompose.response

import com.khanr1.cryocompose as domain
import io.circe.*
import org.http4s.circe.*
import org.http4s.*

final case class Category private (
  id: String,
  name: String,
  description: String,
  parent: Option[String],
)

object Category:
  def create[ID](category: domain.Category[ID]): Category =
    Category(
      category.id.toString(),
      category.name.toString(),
      category.description.toString(),
      category.parent.map(_.toString()),
    )

  given Encoder[Category] = Encoder.forProduct4("id", "name", "desciption", "parent")(c =>
    (c.id, c.name, c.description, c.parent)
  )

  given entityEncoder[F[_]]: EntityEncoder[F, Category] =
    jsonEncoderOf
