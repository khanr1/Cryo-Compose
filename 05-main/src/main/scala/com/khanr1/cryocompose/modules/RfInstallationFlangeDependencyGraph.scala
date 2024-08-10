package com.khanr1
package cryocompose
package modules

import cats.*
import cats.effect.Ref
import cats.syntax.all.*

import com.khanr1.cryocompose.wiring.rf.RfInstallationFlange
import com.khanr1.cryocompose.controllers.inMemory.RfInstallationFlangeController
import com.khanr1.cryocompose.controllers.Controller
import com.khanr1.cryocompose.services.wiring.rf.RfInstallationFlangeService
import com.khanr1.cryocompose.repositories.inMemory.RfInstallationFlangeInMemoryRepository

object RfInstallationFlangeDependencyGraph:
  def make[F[_]: MonadThrow: effect.Async](
    ref: Ref[F, Vector[RfInstallationFlange[Int, Int, Int, Int]]]
  ): F[Controller[F]] =
    RfInstallationFlangeController
      .make[F, Int, Int, Int, Int](
        RfInstallationFlangeService.make[F, Int, Int, Int, Int](
          RfInstallationFlangeInMemoryRepository.make[F](ref)
        )
      )
      .pure
