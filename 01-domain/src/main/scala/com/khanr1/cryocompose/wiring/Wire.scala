package com.khanr1
package cryocompose
package wiring

import squants.space.Length

trait Material

/** A trait representing a wire used in electrical connections.
  *
  * @param material the material used for the wire.
  * @param length the length of the wire, specified either as a direct length or as a standard stage length.
  *               If specified as a direct length, it should be of type [[Length]], whereas if specified as a stage length,
  *               it should be of type [[StageLength]].
  */
trait Wire(material: Material, length: Length | StageLength)
