package com.khanr1
package cryocompose
package wiring
package rf

import cats.Show
import squants.space.Length

/** A final case class representing an RF wire used in electrical connections.
  *
  * This class models an RF wire with specified material and length. The length can either be a direct
  * length of type [[Length]] or a standard stage length of type [[StageLength]].
  *
  * @param material the RF material used for the wire.
  * @param length the length of the RF wire, specified either as a direct length or as a standard stage length.
  *               If specified as a direct length, it should be of type [[Length]], whereas if specified as a stage length,
  *               it should be of type [[StageLength]].
  * @extends Wire An extension of the [[Wire]] trait.
  */
final case class RfWire(material: RFmaterial, length: Length | StageLength)
    extends Wire(material, length):

  /** Generates a unique code for the RF wire by combining its material and length description.
    *
    * @return A string representing the code of the RF wire.
    */
  def code: String =
    val lengthDescription = length match
      case x: Length => x.value.show
      case x: StageLength => x.toString()
    material.toString() + "-" + lengthDescription

  /** Provides a detailed description of the RF wire, including its material and length.
    *
    * @return A string describing the RF wire.
    */
  def description: String =
    val lengthDescription = length match
      case x: Length => x.value.show
      case x: StageLength => x.show
    material.show + " " + lengthDescription

object RfWire:
  /** Generates all possible RF wires with all the staged lengths.
    *
    * This method creates a set of [[RfWire]] instances for all combinations of RF materials and stage lengths,
    * applying certain filters to exclude invalid combinations.
    *
    * @return A set of all possible valid [[RfWire]] instances.
    */
  def generateAll: Set[RfWire] =
    val all = for
      length <- StageLength.values.toSet
      material <- RFmaterial.values.toSet
    yield RfWire(material, length)

    all
      .filterNot(rfwire =>
        rfwire.material == RFmaterial.NbTi_086 && rfwire.length.toString().contains("50K")
      )
      .filterNot(rfwire =>
        rfwire.material == RFmaterial.SCuNi_219 && (rfwire
          .length
          .toString()
          .contains("Still") || rfwire.length.toString().contains("CP"))
      )

  /** Implicit [[Show]] instance for [[RfWire]]. */
  given show: Show[RfWire] = Show.fromToString

  /** Implicit [[Encoder]] instance for [[RfWire]]. */
  given encoder: Encoder[RfWire] =
    Encoder.forProduct2("material", "length")(w => (w.material, w.length))

  /** Implicit [[Decoder]] instance for [[RfWire]]. */
  given decoder: Decoder[RfWire] = Decoder.forProduct2("material", "length")(RfWire(_, _))
