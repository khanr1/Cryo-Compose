package com.khanr1
package cryocompose
package wiring

import squants.space.Length
import io.circe.Encoder
import io.circe.syntax.*
import io.circe.Json
import io.circe.HCursor
import io.circe.DecodingFailure
import squants.space.Millimeters
import cats.Show

trait Material

/** A trait representing a wire used in electrical connections.
  *
  * @param material the material used for the wire.
  * @param length the length of the wire, specified either as a direct length or as a standard stage length.
  *               If specified as a direct length, it should be of type [[Length]], whereas if specified as a stage length,
  *               it should be of type [[StageLength]].
  */
trait Wire(material: Material, length: Length | StageLength)

given encoder: Encoder[Length | StageLength] = new Encoder[Length | StageLength]:
  final def apply(a: Length | StageLength): Json = a match
    case x: Length => Json.fromBigDecimal(x.value)
    case y: StageLength => Json.fromString(y.toString())

given decoder: Decoder[Length | StageLength] = new Decoder[Length | StageLength]:
  final def apply(c: HCursor): Either[DecodingFailure, Length | StageLength] =
    if c.value.isNumber then Right(Millimeters(c.value.asNumber.get.toDouble))
    else if c.value.isString then Right(StageLength.valueOf(c.value.asString.get))
    else Left(DecodingFailure("fail", c.history))

given show: Show[Length | StageLength] = Show.show(s =>
  s match
    case x: Length => x.toString
    case y: StageLength => y.toString().replace("_", "-")
)
