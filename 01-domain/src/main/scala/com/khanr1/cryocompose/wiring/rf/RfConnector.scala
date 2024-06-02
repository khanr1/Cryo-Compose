package com.khanr1
package cryocompose
package wiring
package rf

import cats.{ Show, Eq }
import io.circe.{ Encoder, Decoder }
import squants.time.Frequency

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import io.github.iltotore.iron.circe.given

/** A final case class representing an RF connector.
  *
  * @param id the identifier of the RF connector.
  * @param name the name of the RF connector.
  * @param gender the gender of the RF connector.
  * @param maxFrequency the maximum frequency supported by the RF connector.
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
  connectorName: ConnectorName,
  gender: Gender,
  maxFrequency: Frequency,
  category: CategoryID,
  tags: Set[TagID],
) extends Connector
       with Product[RfConnectorID, CategoryID, TagID]:
  override val numberPin = NumberOfPin(1)
  override val productName = ProductName.applyUnsafe(connectorName.value)
  override val productDescription = ProductDescription.applyUnsafe(connectorName.value)
  override val code = ProductCode.applyUnsafe(connectorCode)
  override val productID = id
  override val categoryID = category
  override val tagsID = tags

object RfConnector:
  /** Show instance for RfConnector, using the default toString representation. */
  given show[RfConnectorID, CategoryID, TagID](
    using
    a: Show[CategoryID],
    b: Show[RfConnectorID],
    c: Show[TagID],
  ): Show[RfConnector[RfConnectorID, CategoryID, TagID]] = Show.fromToString

  /** Eq instance for RfConnector, using universal equality. */
  given eq[RfConnectorID, CategoryID, TagID]: Eq[RfConnector[RfConnectorID, CategoryID, TagID]] =
    Eq.fromUniversalEquals

  /** Encoder instance for RfConnector. */
  given encoder[RfConnectorID: Encoder, CategoryID: Encoder, TagID: Encoder]
    : Encoder[RfConnector[RfConnectorID, CategoryID, TagID]] =
    Encoder.forProduct6("id", "name", "gender", "max frequency", "category", "tags")(r =>
      (r.id, r.connectorName, r.gender, r.maxFrequency, r.category, r.tags)
    )

  /** Decoder instance for RfConnector. */
  given decoder[RfConnectorID: Decoder, CategoryID: Decoder, TagID: Decoder]
    : Decoder[RfConnector[RfConnectorID, CategoryID, TagID]] =
    Decoder.forProduct6("id", "name", "gender", "max frequency", "category", "tags")(
      rf.RfConnector[RfConnectorID, CategoryID, TagID](_, _, _, _, _, _)
    )

/** A final case class representing parameters used to create an RF connector.
  *
  * @param name the name of the RF connector.
  * @param gender the gender of the RF connector.
  * @param maxFrequency the maximum frequency supported by the RF connector.
  * @param category the category identifier associated with the RF connector.
  * @param tags the set of tag identifiers associated with the RF connector.
  * @tparam CategoryID the type of the identifier for categories.
  * @tparam TagID the type of the identifier for tags.
  */
final case class RfConnectorParam[CategoryID, TagID](
  connectorName: ConnectorName,
  gender: Gender,
  maxFrequency: Frequency,
  category: CategoryID,
  tags: Set[TagID],
)

object RfConnectorParam:
  /** Show instance for RfConnectorParam, using the default toString representation. */
  given show[CategoryID, TagID]: Show[RfConnectorParam[CategoryID, TagID]] = Show.fromToString

  /** Eq instance for RfConnectorParam, using universal equality. */
  given eq[CategoryID, TagID]: Eq[RfConnectorParam[CategoryID, TagID]] = Eq.fromUniversalEquals

  /** Encoder instance for RfConnectorParam. */
  given encoder[CategoryID: Encoder, TagID: Encoder]: Encoder[RfConnectorParam[CategoryID, TagID]] =
    Encoder.forProduct5("name", "gender", "max frequency", "category", "tags")(r =>
      (r.connectorName, r.gender, r.maxFrequency, r.category, r.tags)
    )

  /** Decoder instance for RfConnectorParam. */
  given decoder[CategoryID: Decoder, TagID: Decoder]: Decoder[RfConnectorParam[CategoryID, TagID]] =
    Decoder.forProduct5("name", "gender", "max frequency", "category", "tags")(
      rf.RfConnectorParam[CategoryID, TagID](_, _, _, _, _)
    )
