package com.khanr1
package cryocompose
package stages

import cats.Show
import io.circe.*

enum SetStageLength(l: List[StageLength]):
  def segments = this.l

  def from = this.l.head.from
  def to = this.l.last.to

  def setCode = this.toString()

  def description = s"from ${from.show} to ${to.show}"

  case RT_SL_4K extends SetStageLength(List(StageLength.RT_SL_50K, StageLength.`50K_4K`))
  case RT_ISO100_4K extends SetStageLength(List(StageLength.RT_ISO100_50K, StageLength.`50K_4K`))
  case RT_K63_4K extends SetStageLength(List(StageLength.RT_K63_50K, StageLength.`50K_4K`))
  case RT_KF40_4K extends SetStageLength(List(StageLength.RT_KF40_50K, StageLength.`50K_4K`))
  case `4K_Still` extends SetStageLength(List(StageLength.`4K_Still`))
  case Still_MXC_SD extends SetStageLength(List(StageLength.Still_MXC_SD))
  case Still_MXC_SL extends SetStageLength(List(StageLength.Still_CP_SL, StageLength.CP_MXC))
  case Still_MXC_STD extends SetStageLength(List(StageLength.Still_CP, StageLength.CP_MXC))

object SetStageLength:
  given show: Show[SetStageLength] = Show.show(s =>
    s match
      case RT_SL_4K => "RT(SL)-4K"
      case RT_ISO100_4K => "RT(ISO100)-4K"
      case RT_K63_4K => "RT(K63)-4K"
      case RT_KF40_4K => "RT(KF40)-4K"
      case `4K_Still` => "4K-Still"
      case Still_MXC_SD => "Still-MXC(SD)"
      case Still_MXC_SL => "Still-MXC(SL)"
      case Still_MXC_STD => "Still-MXC(STD)"
  )

  given encoder: Encoder[SetStageLength] = new Encoder[SetStageLength]:
    override def apply(a: SetStageLength): Json = Json.fromString(a.show)

  given decoder: Decoder[SetStageLength] = new Decoder[SetStageLength]:
    final def apply(c: HCursor): Decoder.Result[SetStageLength] =
      c.as[String].flatMap {
        case "RT(SL)-4K" => Right(SetStageLength.RT_SL_4K)
        case "RT(ISO100)-4K" => Right(SetStageLength.RT_ISO100_4K)
        case "RT(K63)-4K" => Right(SetStageLength.RT_K63_4K)
        case "RT(KF40)-4K" => Right(SetStageLength.RT_KF40_4K)
        case "4K-Still" => Right(SetStageLength.`4K_Still`)
        case "Still-MXC(SD)" => Right(SetStageLength.Still_MXC_SD)
        case "Still-MXC(SL)" => Right(SetStageLength.Still_MXC_SL)
        case "Still-MXC(STD)" => Right(SetStageLength.Still_MXC_STD)
        case other => Left(DecodingFailure(s"Unknown SetStageLength: $other", c.history))
      }

  def getSetStageLength(stageLengths: List[StageLength]): Option[SetStageLength] =
    SetStageLength.values.find(_.segments == stageLengths)
