package com.khanr1
package cryocompose
package wiring

import monocle.Iso
import com.khanr1.cryocompose.wiring.rf.RfConnector.encoder
import cats.Show

/** Represents bulkhead connector */

trait Bulkhead:
  val connector: Connector
  val length: Length
  val isHermetic: Hermeticity
  def bulkheadCode = "BLK-" + isHermetic.show.head + "-" + connector.connectorCode

enum Hermeticity:
  case Hermetic
  case NonHermetic

object Hermeticity:
  given iso: Iso[Hermeticity, Boolean] = Iso[Hermeticity, Boolean] {
    case Hermetic => true
    case NonHermetic => false
  }(if _ then Hermetic else NonHermetic)

  given encoder: Encoder[Hermeticity] = Encoder
    .encodeString
    .contramap(h =>
      h match
        case Hermetic => "Hermetic"
        case NonHermetic => "NonHermetic"
    )

  given show: Show[Hermeticity] = Show.fromToString

  given decoder: Decoder[Hermeticity] = Decoder.decodeString.map(Hermeticity.valueOf(_))
