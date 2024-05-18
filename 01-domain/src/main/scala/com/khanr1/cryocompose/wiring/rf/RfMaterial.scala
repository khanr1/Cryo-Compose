package com.khanr1
package cryocompose
package wiring
package rf

import cats.Show
import squants.electro.Ohms
import squants.space.Millimeters
import squants.time.Gigahertz
import squants.electro.ElectricalResistance

/** Enumeration representing different RF materials used in cryogenics.
  *
  * @param d Diameter of the material.
  * @param charResistance Characteristic impedance of the material.
  * @param attnRt Attenuation rate at room temperature (RT) for different frequencies.
  * @param attn4K Attenuation rate at 4 Kelvin (4K) for different frequencies.
  */
enum RFmaterial(
  d: Length,
  charResistance: ElectricalResistance,
  attnRt: Map[Frequency, Double],
  attn4K: Map[Frequency, Double],
) extends Material:

  /** @return the diameter of the material */
  def diameter = d

  /** @return the characteristic impedance of the material */
  def characteristicImpedence = charResistance

  /** @return the attenuation rate at room temperature (RT) */
  def attenuation_RT = attnRt

  /** @return the attenuation rate at 4 Kelvin (4K) */
  def attenuation_4K = attn4K

  /** Provides detailed information about the RF material.
    *
    * @return a string containing the details of the material.
    */
  def details: String = s"""\n${this.show}:\n
  Characteristic Impedence: ${this.characteristicImpedence}\n
  Attenuation at RT : ${this.attnRt.mkString("\t")}\n
  Attenuation at 4K : ${this.attnRt.mkString("\t")}\n                                      
  """

  /** @return the code representation of the RF material */
  def code = this.toString()

  /** SCuNi_086 material.
    * - Diameter: 0.86 mm
    * - Characteristic Impedance: 50 Ohms
    * - Attenuation:
    *   - Room Temperature: 1 GHz -> 3.0, 10 GHz -> 9.5
    *   - 4 Kelvin: 1 GHz -> 1.5, 10 GHz -> 4.6
    */
  case SCuNi_086
      extends RFmaterial(
        Millimeters(0.86),
        Ohms.apply(50),
        Map(Gigahertz(1) -> 3.0, Gigahertz(10) -> 9.5),
        Map(Gigahertz(1) -> 1.5, Gigahertz(10) -> 4.6),
      )

  /** SCuNi_219 material.
    * - Diameter: 2.19 mm
    * - Characteristic Impedance: 50 Ohms
    * - Attenuation:
    *   - Room Temperature: 1 GHz -> 1.2, 10 GHz -> 3.8
    *   - 4 Kelvin: 1 GHz -> 0.6, 10 GHz -> 1.8
    */
  case SCuNi_219
      extends RFmaterial(
        Millimeters(2.19),
        Ohms.apply(50),
        Map(Gigahertz(1) -> 1.2, Gigahertz(10) -> 3.8),
        Map(Gigahertz(1) -> 0.6, Gigahertz(10) -> 1.8),
      )

  /** NbTi_086 material.
    * - Diameter: 0.86 mm
    * - Characteristic Impedance: 50 Ohms
    * - Attenuation:
    *   - Room Temperature: 1 GHz -> 9, 10 GHz -> 30.5
    *   - 4 Kelvin: 1 GHz -> 0.5, 10 GHz -> 0.5
    */
  case NbTi_086
      extends RFmaterial(
        Millimeters(0.86),
        Ohms.apply(50),
        Map(Gigahertz(1) -> 9, Gigahertz(10) -> 30.5),
        Map(Gigahertz(1) -> 0.5, Gigahertz(10) -> 0.5),
      )

object RFmaterial:

  /** Show instance for RFmaterial.
    */
  given show: Show[RFmaterial] = Show.show {
    case SCuNi_086 => s"SCuNi-CuNi (${SCuNi_086.diameter})"
    case SCuNi_219 => s"SCuNi-CuNi (${SCuNi_219.diameter})"
    case NbTi_086 => s"NbTi-NbTi (${NbTi_086.diameter})"
  }

  /** Encoder instance for RFmaterial.
    */
  given Encoder[RFmaterial] = Encoder
    .encodeString
    .contramap {
      case SCuNi_086 => "SCuNi-CuNi (0.86 mm)"
      case NbTi_086 => "NbTi-NbTi (0.86 mm)"
      case SCuNi_219 => "SCuNi-CuNi (2.19 mm)"
    }

  /** Decoder instance for RFmaterial.
    */
  given Decoder[RFmaterial] = Decoder
    .decodeString
    .map {
      case "SCuNi-CuNi (0.86 mm)" => SCuNi_086
      case "NbTi-NbTi (0.86 mm)" => NbTi_086
      case "SCuNi-CuNi (2.19 mm)" => SCuNi_219
    }
