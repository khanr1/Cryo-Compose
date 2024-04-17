package com.khanr1
package cryocompose

import cats.Eq
import cats.Show
import io.circe.Decoder
import io.circe.Encoder
import io.github.iltotore.iron.circe.given

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

/** Tag  can be added to a product for adding metadata information
  *
  * @param id
  *   is a unique identifier
  * @param name
  *   is the name of the Tag
  */
final case class Tag[TagID](id: TagID, name: TagName)

object Tag:
  given show[TagID]: Show[Tag[TagID]] = Show.fromToString
  given eq[TagID]: Eq[Tag[TagID]] = Eq.fromUniversalEquals
  given encoder[TagID: Encoder]: Encoder[Tag[TagID]] =
    Encoder.forProduct2("id", "name")(t => (t.id, t.name))

type TagNameR = DescribedAs[Not[Empty], "The name of a tag cannot be empty"]
opaque type TagName = String :| TagNameR
object TagName extends RefinedTypeOps[String, TagNameR, TagName]
