package com.khanr1
package cryocompose
package wiring

import squants.space.Length
import com.khanr1.cryocompose.utils.Material

/** Representation of a wire used in a wiring assembly to connect pins from
  * different material
  *
  * @param material
  *   the material of the wire
  * @param length
  *   the length of the wire
  */
trait Wire(material: Material, length: Length)
