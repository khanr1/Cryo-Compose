package com.khanr1
package cryocompose

import cats.effect.*
import cats.implicits.*
import com.khanr1.cryocompose.controllers.inMemory.TagController
import com.khanr1.cryocompose.modules.*
import org.typelevel.log4cats.Logger
import com.khanr1.cryocompose.wiring.*
import com.khanr1.cryocompose.wiring.rf.*
import com.khanr1.cryocompose.InitialState.rfAssemblyState

object Program:
  def make[F[_]: Logger: Async]: F[Unit] =
    for
      tagRef <- Ref.of[F, Vector[Tag[Int]]](InitialState.tagState)
      catRef <- Ref.of[F, Vector[Category[Int]]](InitialState.catState)
      conRef <- Ref.of[F, Vector[RfConnector[Int, Int, Int]]](InitialState.rfConnectorState)
      rfWire <- Ref.of[F, Vector[RfAssembly[Int, Int, Int, Int]]](InitialState.rfAssemblyState)
      rfSet <- Ref.of[F, Vector[RfSet[Int, Int, Int, Int]]](InitialState.rfSetState)
      tagController <- TagDependencyGraph.make(tagRef)
      categoryController <- CategoryDependencyGraph.make(catRef)
      rfConnectorController <- RfConnectorDependencyGraph.make(conRef)
      rfAssemblyController <- RfAssemblyDependencyGraph.make(rfWire)
      rfSetController <- RfSetDependencyGraph.make(rfSet)
      mainController <- MainDependencyGraph.make
      _ <- HttpServer
        .make(
          HttpApi.make(
            rfAssemblyController,
            rfConnectorController,
            tagController,
            categoryController,
            rfSetController,
            mainController,
          )
        )
        .serve
        .useForever
    yield ()
