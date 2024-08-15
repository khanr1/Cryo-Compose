package com.khanr1
package cryocompose
package wiring
package rf

import cats.Show
import com.khanr1.cryocompose.ports.Ports
import com.khanr1.cryocompose.stages.Stages
import com.khanr1.cryocompose.wiring.rf.RfConnector.decoder

final case class RfInstallationFlange[RfConnectorID, FlangeID, CategoryID, TagID](
  productID: FlangeID,
  port: Ports,
  bulkheads: List[RfBulkhead[RfConnectorID, CategoryID, TagID]],
  stage: Stages,
  categoryID: CategoryID,
  tagsID: Set[TagID],
) extends InstallationFlange
       with Product[FlangeID, CategoryID, TagID]:
  val bulkheadsDistribution =
    bulkheads
      .groupBy(_.productDescription)
      .map((x, y) => (x, y.length))
      .map(x => x._2 + "x" + x._1)
      .mkString(" ")
  override val numberSlot: NumberOfSlot = NumberOfSlot.applyUnsafe(bulkheads.size)
  override val code: ProductCode =
    ProductCode.assume(
      s" RF-INST-$numberSlot-$port-${bulkheads.map(_.code).distinct.mkString("", "|", "")}-$stage"
    )

  override val productName: ProductName = ProductName.assume(
    s"RF Installation ${stage.show} flange with  $bulkheadsDistribution"
  )

  override val productDescription: ProductDescription =
    ProductDescription.applyUnsafe(
      s"RF installation $stage flange for $port with $bulkheadsDistribution"
    )

object RfInstallationFlange:
  given show[RfConnectorID, FlangeID, CategoryID, TagID]
    : Show[RfInstallationFlange[RfConnectorID, FlangeID, CategoryID, TagID]] = Show.fromToString
  given encoder[RfConnectorID: Encoder, FlangeID: Encoder, CategoryID: Encoder, TagID: Encoder]
    : Encoder[RfInstallationFlange[RfConnectorID, FlangeID, CategoryID, TagID]] =
    Encoder.forProduct6("id", "port", "bulkheads", "stage", "category", "tags")(flange =>
      (
        flange.productID,
        flange.port,
        flange.bulkheads,
        flange.stage,
        flange.categoryID,
        flange.tagsID,
      )
    )
  given decoder[RfConnectorID: Decoder, FlangeID: Decoder, CategoryID: Decoder, TagID: Decoder]
    : Decoder[RfInstallationFlange[RfConnectorID, FlangeID, CategoryID, TagID]] =
    Decoder.forProduct6("id", "port", "bulkheads", "stage", "category", "tags")(
      RfInstallationFlange[RfConnectorID, FlangeID, CategoryID, TagID](_, _, _, _, _, _)
    )

final case class RfInstallationFlangeParam[RfConnectorID, CategoryID, TagID](
  port: Ports,
  bulkheads: List[RfBulkhead[RfConnectorID, CategoryID, TagID]],
  stage: Stages,
  categoryID: CategoryID,
  tagsID: Set[TagID],
) extends InstallationFlange:
  override val numberSlot: NumberOfSlot = NumberOfSlot.applyUnsafe(bulkheads.length)

object RfInstallationFlangeParam:
  given show[RfConnectorID, CategoryID, TagID]
    : Show[RfInstallationFlangeParam[RfConnectorID, CategoryID, TagID]] = Show.fromToString
  given decoder[RfConnectorID: Decoder, CategoryID: Decoder, TagID: Decoder]
    : Decoder[RfInstallationFlangeParam[RfConnectorID, CategoryID, TagID]] =
    Decoder.forProduct5("port", "bulkheads", "stage", "category", "tags")(
      RfInstallationFlangeParam[RfConnectorID, CategoryID, TagID](_, _, _, _, _)
    )
