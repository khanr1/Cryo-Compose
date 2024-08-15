package com.khanr1
package cryocompose
package modules

import cats.*
import cats.effect.Ref
import cats.syntax.all.*

import com.khanr1.cryocompose.wiring.rf.RfInstallationSet
import com.khanr1.cryocompose.controllers.inMemory.RfInstallationSetController
import com.khanr1.cryocompose.controllers.Controller
import com.khanr1.cryocompose.services.wiring.rf.RfInstallationSetService
import com.khanr1.cryocompose.repositories.inMemory.RfInstallationSetInMemoryRepository

object RfInstallationSetDependencyGraph:
  def make[F[_]: MonadThrow: effect.Async](
    ref: Ref[F, Vector[RfInstallationSet[Int, Int, Int, Int]]]
  ): F[Controller[F]] =
    RfInstallationSetController
      .make[F, Int, Int, Int, Int](
        RfInstallationSetService.make[F, Int, Int, Int, Int](
          RfInstallationSetInMemoryRepository.make[F](ref)
        )
      )
      .pure
