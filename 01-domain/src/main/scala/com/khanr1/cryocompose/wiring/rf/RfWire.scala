package com.khanr1
package cryocompose
package wiring
package rf

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
    extends Wire(material, length)
