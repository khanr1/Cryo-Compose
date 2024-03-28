package com.khanr1
package cryocompose
package response

import io.circe.*
import io.github.iltotore.iron.circe.given

import org.http4s.*
import org.http4s.circe.*

import com.khanr1.cryocompose as domain

/** A flat version of the Tag
  *
  * @param id
  * @param name
  */
case class Tag(
  id: String,
  name: String,
)

object Tag:
  def apply[TagID](tag: domain.Tag[TagID]): Tag =
    Tag(tag.id.toString(), tag.name.toString())
  given tagEncoder: Encoder[Tag] =
    Encoder.forProduct2(
      "id",
      "name",
    )(t => (t.id, t.name))
  given entityEncoder[F[_]]: EntityEncoder[F, Tag] =
    jsonEncoderOf
