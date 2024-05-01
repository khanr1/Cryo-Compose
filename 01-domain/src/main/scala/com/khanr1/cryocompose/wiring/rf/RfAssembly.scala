package com.khanr1
package cryocompose
package wiring
package rf

import cats.Show
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import com.khanr1.cryocompose.Category.decoder

/** Representation for a RF assembly
  *
  * @param id
  *   the unique identifier of the assembly
  * @param connectors
  *   the RF connector composing the assembly
  * @param lines
  *   the line composing the assembly
  */
final case class RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID](
  id: RfAssemblyID,
  connectors: List[RfConnector[RfConnectorID, CategoryID, TagID]],
  line: RfLine[RfConnectorID, CategoryID, TagID],
  category: CategoryID,
  tags: Set[TagID],
) extends WiringAssembly(connectors, List(line))
       with Product[RfAssemblyID, CategoryID, TagID]:
  override val code: ProductCode =
    val connectorsCode = connectors.map(_.code.show).mkString("(", "-", ")")
    val lineCode = line.toString()
    val full = connectorsCode + "-" + lineCode
    ProductCode.applyUnsafe(full)
  override val productDescription: ProductDescription =
    val text = s"Semi rigid ${connectors.map(_.name).mkString("-")} ${line.wire.description}"
    ProductDescription.applyUnsafe(text)
  override val productID: RfAssemblyID = id
  override val productName: ProductName =
    val text = s"${line.wire.material} ${line.wire.length}"
    ProductName.applyUnsafe(text)
  override val categoryID: CategoryID = category
  override val tagsID: Set[TagID] = tags

object RfAssembly:
  given show[RfAssemblyID, RfConnectorID, CategoryID, TagID]
    : Show[RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID]] = Show.fromToString
  given decoder[RfAssemblyID: Decoder, RfConnectorID: Decoder, CategoryID: Decoder, TagID: Decoder]
    : Decoder[RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID]] =
    Decoder.forProduct5("id", "connectors", "line", "category", "tag")(
      RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID](_, _, _, _, _)
    )
  given encoder[RfAssemblyID: Encoder, RfConnectorID: Encoder, CategoryID: Encoder, TagID: Encoder]
    : Encoder[RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID]] =
    Encoder.forProduct5("id", "connectors", "line", "category", "tag")(a =>
      (a.id, a.connectors, a.line, a.category, a.tags)
    )

/** Used to create RFAssemblyRF assembly
  *
  * @param connectors
  *   the RF connector composing the assembly
  * @param lines
  *   the line composing the assembly
  */
final case class RfAssemblyParam[RfConnectorID, CategoryID, TagID](
  connectors: List[RfConnector[RfConnectorID, CategoryID, TagID]],
  line: RfLine[RfConnectorID, CategoryID, TagID],
  category: CategoryID,
  tags: Set[TagID],
) extends WiringAssembly(connectors, List(line))

object RfAssemblyParam:
  given show[RfConnectorID, CategoryID, TagID]
    : Show[RfAssemblyParam[RfConnectorID, CategoryID, TagID]] =
    Show.fromToString
  given decoder[RfConnectorID: Decoder, CategoryID: Decoder, TagID: Decoder]
    : Decoder[RfAssemblyParam[RfConnectorID, CategoryID, TagID]] =
    Decoder.forProduct4("connectors", "line", "category", "tag")(
      RfAssemblyParam[RfConnectorID, CategoryID, TagID](_, _, _, _)
    )
  given encoder[RfConnectorID: Encoder, CategoryID: Encoder, TagID: Encoder]
    : Encoder[RfAssemblyParam[RfConnectorID, CategoryID, TagID]] =
    Encoder.forProduct4("connectors", "line", "category", "tag")(a =>
      (a.connectors, a.line, a.category, a.tags)
    )
