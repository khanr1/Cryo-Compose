package com.khanr1
package cryocompose

import io.github.iltotore.iron.autoRefine
import com.khanr1.cryocompose.wiring.rf.*
import com.khanr1.cryocompose.wiring.*
import squants.time.Gigahertz

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

  val rfSetState: Vector[RfSet[Int, Int, Int, Int]] = Vector(
    RfSet(1, rfAssemblyState.filter(x => (x.id == 10) || (x.id == 2)).toList, 2, Set(1, 2))
  )
