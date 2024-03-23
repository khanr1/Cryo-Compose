package com.khanr1
package cryocompose
package wiring
package rf

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

final case class RfLine[RfConnectorID](
  connectorA: RfConnector[RfConnectorID],
  connectorB: RfConnector[RfConnectorID],
  wire: RfWire,
) extends Line(
      from = (connectorA, PinIndex(1)),
      to = (connectorB, PinIndex(1)),
      wire,
    )
