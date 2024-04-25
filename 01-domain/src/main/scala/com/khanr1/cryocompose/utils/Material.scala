package com.khanr1
package cryocompose
package utils

import cats.Show
import cats.derived.*

trait Material

enum RFmaterial extends Material derives Show:
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

enum DCmaterial extends Material derives Show:
  case PhBr
  case Cu
