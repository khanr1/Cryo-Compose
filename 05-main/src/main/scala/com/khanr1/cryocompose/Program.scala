package com.khanr1
package cryocompose

import cats.effect.*
import cats.implicits.*
import com.khanr1.cryocompose.controllers.inMemory.TagController
import com.khanr1.cryocompose.modules.*
import io.github.iltotore.iron.autoRefine
import org.typelevel.log4cats.Logger
import com.khanr1.cryocompose.wiring.rf.RfConnector
import com.khanr1.cryocompose.wiring.*

object Program:
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
  )

  val rfConnectorState: Vector[RfConnector[Int, Int, Int]] = Vector(
    RfConnector(1, ConnectorName("SMA"), Gender.Female, 2, Set(2))
  )

  def make[F[_]: Logger: Async]: F[Unit] =
    for
      tagRef <- Ref.of[F, Vector[Tag[Int]]](tagState)
      catRef <- Ref.of[F, Vector[Category[Int]]](catState)
      conRef <- Ref.of[F, Vector[RfConnector[Int, Int, Int]]](rfConnectorState)
      tagController <- TagDependencyGraph.make(tagRef)
      categoryController <- CategoryDependencyGraph.make(catRef)
      rfConnectorController <- RfConnectorDependencyGraph.make(conRef)
      mainController <- MainDependencyGraph.make
      _ <- HttpServer
        .make(
          HttpApi.make(rfConnectorController, tagController, categoryController, mainController)
        )
        .serve
        .useForever
    yield ()
