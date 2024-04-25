package com.khanr1
package cryocompose
package wiring
package rf

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

/** A final case class representing an RF line connecting RF connectors with RF wires.
  *
  * @param connectorA the first RF connector in the RF line.
  * @param connectorB the second RF connector in the RF line.
  * @param wire the RF wire connecting the two RF connectors.
  * @tparam RfConnectorID the type of the identifier for RF connectors.
  * @tparam CategoryID the type of the identifier for categories.
  * @tparam TagID the type of the identifier for tags.
  * @extends Line An extension of the [[Line]] trait.
  */
final case class RfLine[RfConnectorID, CategoryID, TagID](
  connectorA: RfConnector[RfConnectorID, CategoryID, TagID],
  connectorB: RfConnector[RfConnectorID, CategoryID, TagID],
  wire: RfWire,
) extends Line(
      from = (connectorA, PinIndex(1)),
      to = (connectorB, PinIndex(1)),
      wire,
    )
