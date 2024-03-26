package com.khanr1
package cryocompose
package response

import io.circe.*
import io.github.iltotore.iron.circe.given

import org.http4s.*
import org.http4s.circe.*

case class Tag(
  id: String,
  name: String,
)

object Tag:
  def apply[TagID](tag: com.khanr1.cryocompose.Tag[TagID]): Tag =
    Tag(tag.id.toString(), tag.name.toString())
  given tagEncoder: Encoder[Tag] =
    Encoder.forProduct2(
      "id",
      "name",
    )(t => (t.id, t.name))
  given entityEncoder[F[_]]: EntityEncoder[F, Tag] =
    jsonEncoderOf
