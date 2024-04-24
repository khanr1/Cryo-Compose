package com.khanr1
package cryocompose

import cats.Eq
import cats.Show
import io.circe.Decoder
import io.circe.Encoder
import io.github.iltotore.iron.*
import io.github.iltotore.iron.circe.given
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
  given decoder[TagID: Decoder]: Decoder[Tag[TagID]] =
    Decoder.forProduct2("id", "name")(Tag[TagID](_, _))

/** TagParam is used to create a category
  *
  * @param name
  *   name for the Tag
  */
final case class TagParam[TagID](name: TagName)

object TagParam:
  given show[TagID]: Show[TagParam[TagID]] = Show.fromToString
  given eq[TagID]: Eq[TagParam[TagID]] = Eq.fromUniversalEquals
  given decoder[TagID]: Decoder[TagParam[TagID]] =
    Decoder.forProduct1("name")(TagParam[TagID](_))

type TagNameR = DescribedAs[Not[Empty], "The name of a tag cannot be empty"]
opaque type TagName = String :| TagNameR
object TagName extends RefinedTypeOps[String, TagNameR, TagName]
