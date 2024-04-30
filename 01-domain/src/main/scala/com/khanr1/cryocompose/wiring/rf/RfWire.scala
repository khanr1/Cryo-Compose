package com.khanr1
package cryocompose
package wiring
package rf

import cats.Show
import squants.space.Length

/** A final case class representing an RF wire used in electrical connections.
  *
  * @param material the RF material used for the wire.
  * @param length the length of the RF wire, specified either as a direct length or as a standard stage length.
  *               If specified as a direct length, it should be of type [[Length]], whereas if specified as a stage length,
  *               it should be of type [[StageLength]].
  * @extends Wire An extension of the [[Wire]] trait.
  */
final case class RfWire(material: RFmaterial, length: Length | StageLength)
    extends Wire(material, length):
  def description: String =
    val lengthDescription = length match
      case x: Length => x.value.show
      case x: StageLength => x.show

    material.show + " " + lengthDescription

object RfWire:
  def generateAll: Set[RfWire] = for
    length <- StageLength.values.toSet
    material <- RFmaterial.values.toSet
  yield RfWire(material, length)
  given show: Show[RfWire] = Show.fromToString
  given encoder: Encoder[RfWire] =
    Encoder.forProduct2("material", "length")(w => (w.material, w.length))
  given decoder: Decoder[RfWire] = Decoder.forProduct2("material", "length")(RfWire(_, _))
