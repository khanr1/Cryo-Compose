package com.khanr1
package cryocompose
package wiring
package rf

import cats.Show
import squants.electro.Ohms
import squants.space.Millimeters
import squants.space.Length
import squants.electro.ElectricalResistance
import squants.time.Gigahertz

enum RFmaterial(
  d: Length,
  charResistance: ElectricalResistance,
  attnRt: Map[Frequency, Double],
  attn4K: Map[Frequency, Double],
) extends Material:
  def diameter = d
  def characteristicImpedence = charResistance
  def attenuation_RT = attnRt
  def attenuation_4K = attn4K

  def details: String = s"""\n${this.show}:\n
  Characteristic Impedence: ${this.characteristicImpedence}\n
  Attenuation at RT : ${this.attnRt.mkString("\t")}\n
  Attenuation at 4K : ${this.attnRt.mkString("\t")}\n                                      
  """

  case SCuNi_086
      extends RFmaterial(
        Millimeters(0.86),
        Ohms.apply(50),
        Map(Gigahertz(1) -> 3.0, Gigahertz(10) -> 9.5),
        Map(Gigahertz(1) -> 1.5, Gigahertz(10) -> 4.6),
      )
  case SCuNi_219
      extends RFmaterial(
        Millimeters(2.19),
        Ohms.apply(50),
        Map(Gigahertz(1) -> 1.2, Gigahertz(10) -> 3.8),
        Map(Gigahertz(1) -> 0.6, Gigahertz(10) -> 1.8),
      )
  case NbTi_086
      extends RFmaterial(
        Millimeters(0.86),
        Ohms.apply(50),
        Map(Gigahertz(1) -> 9, Gigahertz(10) -> 30.5),
        Map(Gigahertz(1) -> 0.5, Gigahertz(10) -> 0.5),
      )

object RFmaterial:
  given show: Show[RFmaterial] = Show.show(m =>
    m match
      case SCuNi_086 => s"SCuNi-CuNi (${SCuNi_086.diameter})"
      case SCuNi_219 => s"SCuNi-CuNi (${SCuNi_219.diameter})"
      case NbTi_086 => s"NbTi-NbTi (${NbTi_086.diameter})"
  )
  given Encoder[RFmaterial] = Encoder
    .encodeString
    .contramap(m =>
      m match
        case SCuNi_086 => "SCuNi-CuNi (0.86 mm)"
        case NbTi_086 => "NbTi-NbTi (0.86 mm)"
        case SCuNi_219 => "SCuNi-CuNi (2.19 mm)"
    )
  given Decoder[RFmaterial] = Decoder
    .decodeString
    .map(s =>
      s match
        case "SCuNi-CuNi (0.86 mm)" => SCuNi_086
        case "NbTi-NbTi (0.86 mm)" => NbTi_086
        case "SCuNi-CuNi (2.19 mm)" => SCuNi_219
    )
