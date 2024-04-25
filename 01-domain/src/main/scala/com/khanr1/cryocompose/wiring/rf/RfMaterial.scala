package com.khanr1
package cryocompose
package wiring
package rf

import cats.Show

enum RFmaterial extends Material:
  case SCuNi
  case NbTi

object RFmaterial:
  given show: Show[RFmaterial] = Show.fromToString
  given Encoder[RFmaterial] = Encoder.forProduct1("rf_material")(m =>
    m match
      case SCuNi => "SCuNi"
      case NbTi => "NbTi"
  )
  given Decoder[RFmaterial] = Decoder.forProduct1("rf_material")(RFmaterial.valueOf(_))
