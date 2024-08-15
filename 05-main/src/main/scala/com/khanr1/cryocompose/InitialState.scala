package com.khanr1
package cryocompose

import io.github.iltotore.iron.autoRefine
import com.khanr1.cryocompose.wiring.rf.*
import com.khanr1.cryocompose.wiring.*
import squants.time.Gigahertz
import com.khanr1.cryocompose.stages.SetStageLength
import com.khanr1.cryocompose.stages.StageLength
import com.khanr1.cryocompose.stages.Stages
import com.khanr1.cryocompose.ports.Ports
import com.khanr1.cryocompose.stages.SetStageLength.getStageFromSetStageLength

object InitialState:
  val tagState: Vector[Tag[Int]] = Vector(
    Tag(1, TagName("wiring")),
    Tag(2, TagName("RF")),
  )

  val catState: Vector[Category[Int]] = Vector(
    Category(
      1,
      CategoryName("Wiring"),
      CategoryDescription("This category regroup all the wiring"),
      None,
    ),
    Category(
      2,
      CategoryName("RF Wiring"),
      CategoryDescription("This category regroup all the RF lines"),
      Some(1),
    ),
    Category(
      3,
      CategoryName("DC Wiring"),
      CategoryDescription("This category regroup all the DC lines"),
      Some(1),
    ),
    Category(
      4,
      CategoryName("RF Sets"),
      CategoryDescription("This category regroup all the RF Sets"),
      Some(1),
    ),
    Category(
      5,
      CategoryName("RF Flanges"),
      CategoryDescription("This category regroup all the RF flanges"),
      Some(1),
    ),
    Category(
      6,
      CategoryName("RF Bulkheads"),
      CategoryDescription("This category regroup all the RF Bulkheads"),
      Some(1),
    ),
    Category(
      7,
      CategoryName("RF Installation Set"),
      CategoryDescription("This category regroup all the RF installation set"),
      Some(1),
    ),
  ).sortBy(_.name.toString()).reverse

  val rfConnectorState: Vector[RfConnector[Int, Int, Int]] = Vector(
    RfConnector(1, ConnectorName("SMA"), Gender.Female, Gigahertz(18), 2, Set(2)),
    RfConnector(2, ConnectorName("K"), Gender.Female, Gigahertz(40), 2, Set(2)),
  )

  val rfAssemblyState: Vector[RfAssembly[Int, Int, Int, Int]] =
    val rfWires = RfWire.generateAll
    val rfLine = (for
      connectorA <- rfConnectorState
      wire <- rfWires.toVector.sortBy(_.stageLength.get)
    // connectorB <- rfConnectorState
    yield RfLine(connectorA, connectorA, wire)).distinct

    rfLine.map(line =>
      RfAssembly(
        rfLine.indexOf(line) + 1,
        List(line.connectorA, line.connectorB),
        line,
        2,
        Set(1, 2),
      )
    )

  val rfSetState: Vector[RfSet[Int, Int, Int, Int]] =
    // we first get all the setstageLength
    def findRFSet(
      setstage: SetStageLength,
      rfMaterial: RFmaterial,
      rfConnector: ConnectorName,
    ): List[RfAssembly[Int, Int, Int, Int]] =
      val listStage: List[StageLength] = setstage.segments

      listStage.flatMap(l =>
        rfAssemblyState
          .toList
          .filter(as => as.line.wire.material == rfMaterial)
          .filter(as => as.line.connectorA.connectorName == rfConnector)
          .filter(as => as.line.wire.stageLength == Some(l))
      )

    val rfassemblies = (for
      material <- RFmaterial.values.toVector
      setstage <- SetStageLength.values.toVector
      connector <- rfConnectorState.map(_.connectorName)
    yield findRFSet(setstage, material, connector)).filterNot(_.isEmpty)

    rfassemblies.map(line => RfSet(rfassemblies.indexOf(line) + 1, line, 2, Set(1, 2)))

  val rfbulkheadState: Vector[RfBulkhead[Int, Int, Int]] =
    for
      connector <- rfConnectorState
      hermetic <- Hermeticity.values
    yield RfBulkhead(
      Integer.parseInt(
        rfConnectorState.indexOf(connector).toString() + Hermeticity.values.indexOf(hermetic) + 1,
        2,
      ),
      connector,
      Millimeters(10),
      hermetic,
      2,
      Set(2),
    )

  val rfFlangeState: Vector[RfInstallationFlange[Int, Int, Int, Int]] =
    val states = (for
      stage <- Stages.values
      bulkhead <- rfbulkheadState
    // port <- Ports.values
    yield RfInstallationFlange(
      1,
      Ports.KF40,
      List.fill(7)(bulkhead),
      stage,
      1,
      Set(2),
    )).toVector
      .filterNot(x => x.stage.isRT && x.bulkheads.head.isHermetic == Hermeticity.NonHermetic)
      .filterNot(x => !x.stage.isRT && x.bulkheads.head.isHermetic == Hermeticity.Hermetic)
      .filterNot(x => x.stage.isRT && x.stage != Stages.RT_KF40)

    states.map(x => x.copy(productID = states.indexOf(x)))

  val rfInstSetState: Vector[RfInstallationSet[Int, Int, Int, Int]] =
    def findRFInstallationFlange(l: List[Stages], connector: ConnectorName)
      : List[RfInstallationFlange[Int, Int, Int, Int]] =
      l.flatMap(stage =>
        rfFlangeState
          .filter(_.bulkheads.head.connector.connectorName == connector)
          .filter(_.stage == stage)
      ).toList

    val stageArray = SetStageLength
      .values
      .map(getStageFromSetStageLength(_).distinct)
      .filterNot(_.contains(Stages.RT_SL))
      .filterNot(_.contains(Stages.RT_ISO100))
      .filterNot(_.contains(Stages.RT_K63))
      .toList
    val states = (for
      stages <- stageArray
      connector <- rfConnectorState
    yield RfInstallationSet[Int, Int, Int, Int](
      1,
      findRFInstallationFlange(stages, connector.connectorName),
      1,
      Set(1),
    )).toVector

    states.map(x => x.copy(productID = states.indexOf(x)))
