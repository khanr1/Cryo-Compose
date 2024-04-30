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
  given Encoder[RFmaterial] = Encoder
    .encodeString
    .contramap(m =>
      m match
        case SCuNi => "SCuNi"
        case NbTi => "NbTi"
    )
  given Decoder[RFmaterial] = Decoder.decodeString.map(RFmaterial.valueOf(_))
