package com.khanr1
package cryocompose
package ports

import cats.Show
import io.circe.*
import io.circe.Decoder.Result

enum Ports:
  case KF40
  case K63
  case K25
  case ISO100
  case K16
  case SL

object Ports:
  given show: Show[Ports] = Show.show(p =>
    p match
      case KF40 => "KF40"
      case K63 => "K63"
      case K25 => "K25"
      case ISO100 => "ISO100"
      case K16 => "K16"
      case SL => "SL"
  )

  given encoder: Encoder[Ports] = new Encoder[Ports]:
    override def apply(a: Ports): Json = Json.fromString(a.show)

  given decoder: Decoder[Ports] = new Decoder[Ports]:
    override def apply(c: HCursor): Result[Ports] = for
      stringValue <- c.as[String]
      port <- stringValue match
        case "KF40" => Right(KF40)
        case "K63" => Right(K63)
        case "K25" => Right(K25)
        case "ISO100" => Right(ISO100)
        case "K16" => Right(K16)
        case "SL" => Right(SL)
        case other => Left(DecodingFailure(s"Invalid port: $other", c.history))
    yield port
