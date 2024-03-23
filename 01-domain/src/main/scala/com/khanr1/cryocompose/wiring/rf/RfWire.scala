package com.khanr1
package cryocompose
package wiring
package rf

import com.khanr1.cryocompose.utils.RFmaterial
import squants.space.Length

final case class RfWire(material: RFmaterial, length: Length) extends Wire(material, length)
