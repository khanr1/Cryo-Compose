package com.khanr1
package cryocompose
package modules

import cats.*
import cats.effect.Ref
import cats.syntax.all.*

import com.khanr1.cryocompose.wiring.rf.RfConnector
import com.khanr1.cryocompose.controllers.inMemory.RfConnectorController
import com.khanr1.cryocompose.controllers.Controller
import com.khanr1.cryocompose.services.wiring.rf.RfConnectorService
import com.khanr1.cryocompose.repositories.inMemory.RfConnectorInMemoryRepository

object RfConnectorDependencyGraph:
  def make[F[_]: MonadThrow: effect.Async](ref: Ref[F, Vector[RfConnector[Int, Int, Int]]])
    : F[Controller[F]] =
    RfConnectorController
      .make[F, Int, Int, Int](
        RfConnectorService.make[F, Int, Int, Int](
          RfConnectorInMemoryRepository.make[F](ref)
        )
      )
      .pure
