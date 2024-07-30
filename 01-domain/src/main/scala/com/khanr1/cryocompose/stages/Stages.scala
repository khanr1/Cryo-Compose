package com.khanr1
package cryocompose
package stages

import cats.Show
import io.circe.Json
import io.circe.HCursor
import io.circe.Decoder.Result
import io.circe.DecodingFailure

/** Enumerate all the stages that are in a dilution fridge.
  */

enum Stages:
  def isRT = this match
    case RT_SL => true
    case RT_ISO100 => true
    case RT_K63 => true
    case RT_KF40 => true
    case _ => false

  case RT_SL
  case RT_ISO100
  case RT_K63
  case RT_KF40
  case `50K`
  case `4K`
  case Still
  case CP
  case MXC

object Stages:

  // define the Stages that are the one defining the different stage in a Cryostat
  def referenceStages: List[Stages] =
    List(RT_SL, RT_ISO100, RT_K63, RT_KF40, Stages.`4K`, Stages.Still, Stages.MXC)
  given show: Show[Stages] = Show.show(s =>
    s match
      case RT_SL => "RT(SL)"
      case RT_ISO100 => "RT(ISO100)"
      case RT_K63 => "RT(K63)"
      case RT_KF40 => "RT(KF40)"
      case `50K` => "50K"
      case `4K` => "4K"
      case Still => "Still"
      case CP => "CP"
      case MXC => "MXC"
  )

  given encoder: Encoder[Stages] = new Encoder[Stages]:
    override def apply(stage: Stages): Json = Json.fromString(stage.show)

  given decoder: Decoder[Stages] = new Decoder[Stages]:
    override def apply(c: HCursor): Result[Stages] =
      for
        stringValue <- c.as[String]
        stage <- stringValue match
          case "RT (KF40)" => Right(Stages.RT_KF40)
          case "RT (K63)" => Right(Stages.RT_K63)
          case "RT (ISO100)" => Right(Stages.RT_ISO100)
          case "RT (SL)" => Right(Stages.RT_SL)
          case "50K" => Right(Stages.`50K`)
          case "4K" => Right(Stages.`4K`)
          case "Still" => Right(Stages.Still)
          case "CP" => Right(Stages.CP)
          case "MXC" => Right(Stages.MXC)
          case other => Left(DecodingFailure(s"Invalid stage: $other", c.history))
      yield stage

  given Ordering[Stages] with
    def compare(x: Stages, y: Stages): Int =
      import Stages.*
      val order = Map(
        RT_SL -> 0,
        RT_ISO100 -> 1,
        RT_K63 -> 2,
        RT_KF40 -> 3,
        `50K` -> 4,
        `4K` -> 5,
        Still -> 6,
        CP -> 7,
        MXC -> 8,
      )
      order(x) compare order(y)
