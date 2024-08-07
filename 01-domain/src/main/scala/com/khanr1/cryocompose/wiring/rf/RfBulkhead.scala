package com.khanr1
package cryocompose
package wiring
package rf

import cats.{ Show, Eq }
import io.circe.{ Encoder, Decoder }

final case class RfBulkhead[RfConnectorID, CategoryID, TagID](
  id: RfConnectorID,
  connector: RfConnector[RfConnectorID, CategoryID, TagID],
  length: Length,
  isHermetic: Hermeticity,
  category: CategoryID,
  tags: Set[TagID],
) extends Bulkhead
       with Product[RfConnectorID, CategoryID, TagID]:
  override val categoryID: CategoryID = category
  override val productID: RfConnectorID = id
  override val code: ProductCode = ProductCode.assume(bulkheadCode)
  override val productDescription: ProductDescription = ProductDescription.assume(
    s"${connector.connectorName}| ${connector.gender.show} ${isHermetic.show} bulkhead connector"
  )
  override val tagsID: Set[TagID] = tags
  override val productName: ProductName =
    ProductName.assume(s"${connector.gender.show} ${isHermetic.show} bulkhead connector")

object RfBulkhead:
  /** Show instance for RfBulkhead, using the default toString representation. */
  given show[RfConnectorID, CategoryID, TagID](
    using
    a: Show[CategoryID],
    b: Show[RfConnectorID],
    c: Show[TagID],
  ): Show[RfBulkhead[RfConnectorID, CategoryID, TagID]] = Show.fromToString

  /** Eq instance for RfBulkhead, using universal equality. */
  given eq[RfConnectorID, CategoryID, TagID]: Eq[RfBulkhead[RfConnectorID, CategoryID, TagID]] =
    Eq.fromUniversalEquals

  /** Json Decoder */
  given decoder[RfConnectorID: Decoder, CategoryID: Decoder, TagID: Decoder]
    : Decoder[RfBulkhead[RfConnectorID, CategoryID, TagID]] =
    Decoder.forProduct6("id", "connector", "length", "isHermetic", "category", "tags")(
      RfBulkhead[RfConnectorID, CategoryID, TagID](_, _, _, _, _, _)
    )
    /** Json encpder */
  given encoder[RfConnectorID: Encoder, CategoryID: Encoder, TagID: Encoder]
    : Encoder[RfBulkhead[RfConnectorID, CategoryID, TagID]] =
    Encoder.forProduct6("id", "connector", "length", "isHermetic", "category", "tags")(blk =>
      (blk.id, blk.connector, blk.length, blk.isHermetic, blk.category, blk.tags)
    )

final case class RfBulkheadParam[RfConnectorID, CategoryID, TagID](
  connector: RfConnector[RfConnectorID, CategoryID, TagID],
  length: Length,
  isHermetic: Hermeticity,
  category: CategoryID,
  tags: Set[TagID],
) extends Bulkhead

object RfBulkheadParam:
  /** Show instance for RfBulkhead, using the default toString representation. */
  given show[RfConnectorID, CategoryID, TagID](
    using
    a: Show[CategoryID],
    b: Show[RfConnectorID],
    c: Show[TagID],
  ): Show[RfBulkheadParam[RfConnectorID, CategoryID, TagID]] = Show.fromToString

  /** Eq instance for RfBulkhead, using universal equality. */
  given eq[RfConnectorID, CategoryID, TagID]
    : Eq[RfBulkheadParam[RfConnectorID, CategoryID, TagID]] =
    Eq.fromUniversalEquals

  /** Json Decoder */
  given decoder[RfConnectorID: Decoder, CategoryID: Decoder, TagID: Decoder]
    : Decoder[RfBulkheadParam[RfConnectorID, CategoryID, TagID]] =
    Decoder.forProduct5("connector", "length", "isHermetic", "category", "tags")(
      RfBulkheadParam[RfConnectorID, CategoryID, TagID](_, _, _, _, _)
    )
    /** Json encpder */
  given encoder[RfConnectorID: Encoder, CategoryID: Encoder, TagID: Encoder]
    : Encoder[RfBulkheadParam[RfConnectorID, CategoryID, TagID]] =
    Encoder.forProduct5("connector", "length", "isHermetic", "category", "tags")(blk =>
      (blk.connector, blk.length, blk.isHermetic, blk.category, blk.tags)
    )
