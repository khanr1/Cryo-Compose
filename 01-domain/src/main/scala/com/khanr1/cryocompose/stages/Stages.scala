package com.khanr1
package cryocompose
package stages

import cats.Show
import io.circe.Json
import io.circe.HCursor
import io.circe.Decoder.Result
import io.circe.DecodingFailure

enum Stages:
  case RF_SL
  case RT_ISO100
  case RT_K63
  case RT_KF40
  case `50K`
  case `4K`
  case Still
  case CP
  case CP_SD
  case CP_XLD_SL
  case MXC

object Stages:
  given show: Show[Stages] = Show.show(s =>
    s match
      case RT_KF40 => "RF (KF40)"
      case RT_K63 => "RF (K63)"
      case RT_ISO100 => "RF (ISO100)"
      case RF_SL => "RF (SL)"
      case `50K` => "50K"
      case `4K` => "4K"
      case Still => "Still"
      case CP => "CP"
      case CP_SD => "CP (SD)"
      case CP_XLD_SL => "CP (XLD-SL)"
      case MXC => "MXC"
  )

  given encoder: Encoder[Stages] = new Encoder[Stages]:
    override def apply(stage: Stages): Json = Json.fromString(stage.show)

  given decoder: Decoder[Stages] = new Decoder[Stages]:
    override def apply(c: HCursor): Result[Stages] =
      for
        stringValue <- c.as[String]
        stage <- stringValue match
          case "RF (KF40)" => Right(Stages.RT_KF40)
          case "RF (K63)" => Right(Stages.RT_K63)
          case "RF (ISO100)" => Right(Stages.RT_ISO100)
          case "RF (SL)" => Right(Stages.RF_SL)
          case "50K" => Right(Stages.`50K`)
          case "4K" => Right(Stages.`4K`)
          case "Still" => Right(Stages.Still)
          case "CP" => Right(Stages.CP)
          case "CP (SD)" => Right(Stages.CP_SD)
          case "CP (XLD-SL)" => Right(Stages.CP_XLD_SL)
          case "MXC" => Right(Stages.MXC)
          case other => Left(DecodingFailure(s"Invalid stage: $other", c.history))
      yield stage

  given Ordering[Stages] with
    def compare(x: Stages, y: Stages): Int =
      import Stages.*
      val order = Map(
        RF_SL -> 0,
        RT_ISO100 -> 1,
        RT_K63 -> 2,
        RT_KF40 -> 3,
        `50K` -> 4,
        `4K` -> 5,
        Still -> 6,
        CP -> 7,
        CP_SD -> 8,
        CP_XLD_SL -> 9,
        MXC -> 10,
      )
      order(x) compare order(y)
