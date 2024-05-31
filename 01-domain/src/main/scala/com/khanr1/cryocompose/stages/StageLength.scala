package com.khanr1
package cryocompose
package stages

import cats.Show
import io.circe.*
import com.khanr1.cryocompose.wiring.rf.RFmaterial
import squants.space.Length

enum StageLength(fromStage: Stages, toStage: Stages):
  val from = this.fromStage
  val to = this.toStage

  def isUpper: Boolean = this match
    case RT_SL_50K => true
    case RT_ISO100_50K => true
    case RT_K63_50K => true
    case RT_KF40_50K => true
    case `50K_4K` => true
    case _ => false

  def code: String = this.toString()

  def description: String = s"from ${this.from.show} to ${this.to.show} "

  case RT_SL_50K extends StageLength(Stages.RT_SL, Stages.`50K`)
  case RT_ISO100_50K extends StageLength(Stages.RT_ISO100, Stages.`50K`)
  case RT_K63_50K extends StageLength(Stages.RT_K63, Stages.`50K`)
  case RT_KF40_50K extends StageLength(Stages.RT_KF40, Stages.`50K`)
  case `50K_4K` extends StageLength(Stages.`50K`, Stages.`4K`)
  case `4K_Still` extends StageLength(Stages.`4K`, Stages.Still)
  case Still_CP extends StageLength(Stages.Still, Stages.CP)
  case Still_CP_SL extends StageLength(Stages.Still, Stages.CP)
  case Still_MXC_SD extends StageLength(Stages.Still, Stages.MXC)
  case CP_MXC extends StageLength(Stages.CP, Stages.MXC)

object StageLength:
  given show: Show[StageLength] = Show.show(sl =>
    sl match
      case RT_SL_50K => "RT(SL)-50K"
      case RT_ISO100_50K => "RT(ISO100)-50K"
      case RT_K63_50K => "RT(K63)-50K"
      case RT_KF40_50K => "RT(KF40)-50K"
      case `50K_4K` => "50K-4K"
      case `4K_Still` => "4K-Still"
      case Still_CP => "Still-CP"
      case Still_CP_SL => "Still-CP(SL)"
      case Still_MXC_SD => "Still-MXC(SD)"
      case CP_MXC => "CP-MXC"
  )
  given encoder: Encoder[StageLength] = new Encoder[StageLength]:
    override def apply(a: StageLength): Json = Json.fromString(a.show)

  given decoder: Decoder[StageLength] = new Decoder[StageLength]:
    final def apply(c: HCursor): Decoder.Result[StageLength] =
      c.as[String].flatMap {
        case "RT(SL)-50K" => Right(RT_SL_50K)
        case "RT(ISO100)-50K" => Right(RT_ISO100_50K)
        case "RT(K63)-50K" => Right(RT_K63_50K)
        case "RT(KF40)-50K" => Right(RT_KF40_50K)
        case "50K-4K" => Right(`50K_4K`)
        case "4K-Still" => Right(`4K_Still`)
        case "Still-CP" => Right(Still_CP)
        case "Still-CP(SL)" => Right(Still_CP_SL)
        case "Still-MXC(SD)" => Right(Still_MXC_SD)
        case "CP-MXC" => Right(CP_MXC)
        case other => Left(DecodingFailure(s"Unknown StageLength: $other", c.history))
      }

  given Ordering[StageLength] with
    def compare(x: StageLength, y: StageLength): Int =
      import StageLength.*
      val order = Map(
        RT_SL_50K -> 0,
        RT_ISO100_50K -> 1,
        RT_K63_50K -> 2,
        RT_KF40_50K -> 3,
        `50K_4K` -> 4,
        `4K_Still` -> 5,
        Still_MXC_SD -> 6,
        Still_CP -> 7,
        Still_CP_SL -> 8,
        CP_MXC -> 9,
      )
      order(x) compare order(y)

def rfStageLength: Map[(StageLength, RFmaterial), Length] = Map(
  (StageLength.RT_SL_50K, RFmaterial.SCuNi_086) -> Millimeters(290),
  (StageLength.RT_SL_50K, RFmaterial.SCuNi_219) -> Millimeters(245),
  (StageLength.RT_ISO100_50K, RFmaterial.SCuNi_086) -> Millimeters(290),
  (StageLength.RT_ISO100_50K, RFmaterial.SCuNi_219) -> Millimeters(245),
  (StageLength.RT_K63_50K, RFmaterial.SCuNi_086) -> Millimeters(275),
  (StageLength.RT_K63_50K, RFmaterial.SCuNi_219) -> Millimeters(245),
  (StageLength.RT_KF40_50K, RFmaterial.SCuNi_086) -> Millimeters(330),
  (StageLength.RT_KF40_50K, RFmaterial.SCuNi_219) -> Millimeters(300),
  (StageLength.`50K_4K`, RFmaterial.SCuNi_086) -> Millimeters(355),
  (StageLength.`50K_4K`, RFmaterial.SCuNi_219) -> Millimeters(330),
  (StageLength.`4K_Still`, RFmaterial.SCuNi_086) -> Millimeters(275),
  (StageLength.`4K_Still`, RFmaterial.NbTi_086) -> Millimeters(275),
  (StageLength.Still_CP, RFmaterial.SCuNi_086) -> Millimeters(160),
  (StageLength.Still_CP, RFmaterial.NbTi_086) -> Millimeters(160),
  (StageLength.Still_CP_SL, RFmaterial.SCuNi_086) -> Millimeters(230),
  (StageLength.Still_CP_SL, RFmaterial.NbTi_086) -> Millimeters(230),
  (StageLength.Still_MXC_SD, RFmaterial.SCuNi_086) -> Millimeters(275),
  (StageLength.Still_MXC_SD, RFmaterial.NbTi_086) -> Millimeters(275),
  (StageLength.CP_MXC, RFmaterial.SCuNi_086) -> Millimeters(230),
  (StageLength.CP_MXC, RFmaterial.NbTi_086) -> Millimeters(230),
)
