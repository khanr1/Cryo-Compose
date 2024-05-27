package com.khanr1
package cryocompose

import io.github.iltotore.iron.autoRefine
import com.khanr1.cryocompose.wiring.rf.*
import com.khanr1.cryocompose.wiring.*
import squants.time.Gigahertz
import com.khanr1.cryocompose.stages.SetStageLength

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
    RfConnector(1, ConnectorName("SMA"), Gender.Female, Gigahertz(18), 2, Set(2))
  )

  val rfAssemblyState: Vector[RfAssembly[Int, Int, Int, Int]] =
    val rfWires = RfWire.generateAll
    val rfLine = (for
      wire <- rfWires.toVector
      connectorA <- rfConnectorState
      connectorB <- rfConnectorState
    yield RfLine(connectorA, connectorB, wire)).distinct

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
    val stageSetLengths = SetStageLength.values.toVector
    // then we get all the stageLenght from each Stage
    val StageLengths = stageSetLengths.map(_.segments)
    // then for each we look at the RF assembly that are compatible with this stage length for each
    def mapStageLengthsToRFAssemblies(
      material: RFmaterial
    ) =
      StageLengths.map { stageLengths =>
        stageLengths.flatMap { stageLength =>
          rfAssemblyState.filter(rfAssembly =>
            rfAssembly.line.wire.length == stageLength && rfAssembly.line.wire.material == material
          )
        }
      }
    val listRfAssemblies = RFmaterial
      .values
      .toList
      .flatMap(mapStageLengthsToRFAssemblies(_))
      .filterNot(_.isEmpty)

    listRfAssemblies.map(x => RfSet(listRfAssemblies.indexOf(x) + 1, x, 2, Set(1, 2))).toVector
