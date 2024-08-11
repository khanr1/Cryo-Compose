package com.khanr1
package cryocompose
package wiring.rf

import com.khanr1.cryocompose.stages.StageLength.getStageLength
import cats.Show

final case class RfInstallationSet[ProductID, FlangeID, CategoryID, TagID](
  productID: ProductID,
  rfInstallationFlanges: List[RfInstallationFlange[ProductID, FlangeID, CategoryID, TagID]],
  categoryID: CategoryID,
  tagsID: Set[TagID],
) extends Product[ProductID, CategoryID, TagID]:
  /** The code representing the connectors in the RF set. */
  val connectorCode: String =
    rfInstallationFlanges.head.bulkheads.map(_.connector.connectorName).mkString("-")
  val portCode: String =
    rfInstallationFlanges.map(_.port).distinct.mkString("-")
  val lengthCode: String =
    val liststage = rfInstallationFlanges.sortBy(_.stage).map(_.stage)
    getStageLength(liststage.head, liststage.last).mkString("->")
  /** The string representation of the RF set elements, sorted by wire length. */
  val setElement: String = rfInstallationFlanges
    .sortBy(_.stage)
    .map(rfAssembly => rfAssembly.productName.value)
    .mkString("-", "\n-", "")
  override val code: ProductCode =
    ProductCode.applyUnsafe(s"RF-INST-SET-$portCode-$connectorCode-$lengthCode")
  override val productDescription: ProductDescription = ProductDescription.applyUnsafe(
    s"RF Installation set $connectorCode $portCode :\n\n$setElement"
  )
  override val productName: ProductName =
    ProductName.applyUnsafe(s"RF installation set $portCode $connectorCode $lengthCode")

object RfInstallationSet:
  given show[ProductID, FlangeID, CategoryID, TagID]
    : Show[RfInstallationSet[ProductID, FlangeID, CategoryID, TagID]] = Show.fromToString
  given encoder[ProductID: Encoder, FlangeID: Encoder, CategoryID: Encoder, TagID: Encoder]
    : Encoder[RfInstallationSet[ProductID, FlangeID, CategoryID, TagID]] =
    Encoder.forProduct4("id", "rfInstallationFlange", "category", "tags")(set =>
      (set.productID, set.rfInstallationFlanges, set.categoryID, set.tagsID)
    )
  given decoder[ProductID: Decoder, FlangeID: Decoder, CategoryID: Decoder, TagID: Decoder]
    : Decoder[RfInstallationSet[ProductID, FlangeID, CategoryID, TagID]] =
    Decoder.forProduct4("id", "rfInstallationFlange", "category", "tags")(
      RfInstallationSet[ProductID, FlangeID, CategoryID, TagID](_, _, _, _)
    )

final case class RfInstallationSetParam[ProductID, FlangeID, CategoryID, TagID](
  rfInstallationFlanges: List[RfInstallationFlange[ProductID, FlangeID, CategoryID, TagID]],
  categoryID: CategoryID,
  tagsID: Set[TagID],
)
object RfInstallationSetParam:
  given show[ProductID, FlangeID, CategoryID, TagID]
    : Show[RfInstallationSetParam[ProductID, FlangeID, CategoryID, TagID]] = Show.fromToString
  given encoder[ProductID: Encoder, FlangeID: Encoder, CategoryID: Encoder, TagID: Encoder]
    : Encoder[RfInstallationSetParam[ProductID, FlangeID, CategoryID, TagID]] =
    Encoder.forProduct3("rfInstallationFlange", "category", "tags")(set =>
      (set.rfInstallationFlanges, set.categoryID, set.tagsID)
    )
  given decoder[ProductID: Decoder, FlangeID: Decoder, CategoryID: Decoder, TagID: Decoder]
    : Decoder[RfInstallationSetParam[ProductID, FlangeID, CategoryID, TagID]] =
    Decoder.forProduct3("rfInstallationFlange", "category", "tags")(
      RfInstallationSetParam[ProductID, FlangeID, CategoryID, TagID](_, _, _)
    )
