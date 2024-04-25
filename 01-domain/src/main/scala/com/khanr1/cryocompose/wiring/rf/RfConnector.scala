package com.khanr1
package cryocompose
package wiring
package rf

import cats.{ Show, Eq }
import io.circe.Encoder

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import io.github.iltotore.iron.circe.given

import squants.electro.ElectricalResistance
import squants.time.Frequency

/** Represents the RF connector
  *
  * @param name
  *   the name of the connector
  * @param gender
  *   The gender of the connector
  */
final case class RfConnector[RfConnectorID, CategoryID, TagID](
  id: RfConnectorID,
  name: ConnectorName,
  gender: Gender,
  category: CategoryID,
  tags: Set[TagID],
) extends Connector(name, gender, numberPin = NumberOfPin(1))
       with Product[RfConnectorID, CategoryID, TagID]:
  override val productName = ProductName.applyUnsafe(name.value)
  override val productDescription = ProductDescription.applyUnsafe(name.value)
  override val code = ProductCode.applyUnsafe(name.value)
  override val productID = id
  override val categoryID = category
  override val tagsID = tags

  object RfConnector:
    given show: Show[RfConnector[RfConnectorID, CategoryID, TagID]] = Show.fromToString
    given eq: Eq[RfConnector[RfConnectorID, CategoryID, TagID]] = Eq.fromUniversalEquals
    given encoder[RfConnectorID: Encoder, CategoryID: Encoder, TagID: Encoder]
      : Encoder[RfConnector[RfConnectorID, CategoryID, TagID]] =
      Encoder.forProduct5("id", "name", "gender", "category", "tags")(r =>
        (r.id, r.name, r.gender, r.category, r.tags)
      )
