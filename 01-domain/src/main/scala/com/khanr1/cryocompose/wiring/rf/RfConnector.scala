package com.khanr1
package cryocompose
package wiring
package rf

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

import squants.electro.ElectricalResistance
import squants.time.Frequency

/** Represents the RF connector
  *
  * @param name
  *   the name of the connector
  * @param gender
  *   The gender of the connector
  */
case class RfConnector[RfConnectorID](
  id: RfConnectorID,
  name: ConnectorName,
  gender: Gender,
) extends Connector(name, gender, numberPin = NumberOfPin(1))
       with Product[RfConnectorID]:
  override val productName = ProductName.applyUnsafe(name.value)
  override val productDescription = ProductDescription.applyUnsafe(name.value)
  override val code = ProductCode.applyUnsafe(name.value)
  override val productID = id
