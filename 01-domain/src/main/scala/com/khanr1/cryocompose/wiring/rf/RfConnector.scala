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
import squants.time.Hertz

/** A final case class representing an RF connector.
  *
  * @param id the identifier of the RF connector.
  * @param name the name of the RF connector.
  * @param gender the gender of the RF connector.
  * @param category the category identifier associated with the RF connector.
  * @param tags the set of tag identifiers associated with the RF connector.
  * @tparam RfConnectorID the type of the identifier for RF connectors.
  * @tparam CategoryID the type of the identifier for categories.
  * @tparam TagID the type of the identifier for tags.
  * @extends Connector An extension of the [[Connector]] trait.
  * @extends Product An extension of the [[Product]] trait.
  */
final case class RfConnector[RfConnectorID, CategoryID, TagID](
  id: RfConnectorID,
  name: ConnectorName,
  gender: Gender,
  maxFrequency: Frequency,
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
  given show[RfConnectorID, CategoryID, TagID](
    using
    a: Show[CategoryID],
    b: Show[RfConnectorID],
    c: Show[TagID],
  ): Show[RfConnector[RfConnectorID, CategoryID, TagID]] = Show.fromToString

  given eq[RfConnectorID, CategoryID, TagID]: Eq[RfConnector[RfConnectorID, CategoryID, TagID]] =
    Eq.fromUniversalEquals
  given encoder[RfConnectorID: Encoder, CategoryID: Encoder, TagID: Encoder]
    : Encoder[RfConnector[RfConnectorID, CategoryID, TagID]] =
    Encoder.forProduct6("id", "name", "gender", "max frequency", "category", "tags")(r =>
      (r.id, r.name, r.gender, r.maxFrequency, r.category, r.tags)
    )
  given decoder[RfConnectorID: Decoder, CategoryID: Decoder, TagID: Decoder]
    : Decoder[RfConnector[RfConnectorID, CategoryID, TagID]] =
    Decoder.forProduct6("id", "name", "gender", "max frequency", "category", "tags")(
      rf.RfConnector[RfConnectorID, CategoryID, TagID](_, _, _, _, _, _)
    )

/** A final case class representing parameters used to create an RF connector.
  *
  * @param name the name of the RF connector.
  * @param gender the gender of the RF connector.
  * @param category the category identifier associated with the RF connector.
  * @param tags the set of tag identifiers associated with the RF connector.
  * @tparam CategoryID the type of the identifier for categories.
  * @tparam TagID the type of the identifier for tags.
  */
final case class RfConnectorParam[CategoryID, TagID](
  name: ConnectorName,
  gender: Gender,
  maxFrequency: Frequency,
  category: CategoryID,
  tags: Set[TagID],
)

object RfConnectorParam:
  given show[CategoryID, TagID]: Show[RfConnectorParam[CategoryID, TagID]] = Show.fromToString
  given eq[CategoryID, TagID]: Eq[RfConnectorParam[CategoryID, TagID]] = Eq.fromUniversalEquals
  given encoder[CategoryID: Encoder, TagID: Encoder]: Encoder[RfConnectorParam[CategoryID, TagID]] =
    Encoder.forProduct5("name", "gender", "max frequency", "category", "tags")(r =>
      (r.name, r.gender, r.maxFrequency, r.category, r.tags)
    )
  given decoder[CategoryID: Decoder, TagID: Decoder]: Decoder[RfConnectorParam[CategoryID, TagID]] =
    Decoder.forProduct5("name", "gender", "max frequency", "category", "tags")(
      rf.RfConnectorParam[CategoryID, TagID](_, _, _, _, _)
    )
