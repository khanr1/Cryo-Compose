package com.khanr1
package cryocompose
package wiring
package rf

import squants.space.Length

final case class RfWire(material: RFmaterial, length: Length) extends Wire(material, length)
