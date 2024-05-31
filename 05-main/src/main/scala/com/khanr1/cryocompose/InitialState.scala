package com.khanr1
package cryocompose

import io.github.iltotore.iron.autoRefine
import com.khanr1.cryocompose.wiring.rf.*
import com.khanr1.cryocompose.wiring.*
import squants.time.Gigahertz
import com.khanr1.cryocompose.stages.SetStageLength
import com.khanr1.cryocompose.stages.StageLength

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
          .filter(as => as.line.connectorA.name == rfConnector)
          .filter(as => as.line.wire.stageLength == Some(l))
      )

    val rfassemblies = (for
      material <- RFmaterial.values.toVector
      setstage <- SetStageLength.values.toVector
      connector <- rfConnectorState.map(_.name)
    yield findRFSet(setstage, material, connector)).filterNot(_.isEmpty)

    rfassemblies.map(line => RfSet(rfassemblies.indexOf(line) + 1, line, 2, Set(1, 2)))
