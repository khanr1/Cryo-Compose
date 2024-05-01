package com.khanr1
package cryocompose
package modules

import cats.*
import cats.effect.Ref
import cats.syntax.all.*

import com.khanr1.cryocompose.wiring.rf.RfAssembly
import com.khanr1.cryocompose.controllers.inMemory.RfAssemblyController
import com.khanr1.cryocompose.controllers.Controller
import com.khanr1.cryocompose.services.wiring.rf.RfAssemblyService
import com.khanr1.cryocompose.repositories.inMemory.RfAssemblyInMemoryRepository

object RfAssemblyDependencyGraph:
  def make[F[_]: MonadThrow: effect.Async](ref: Ref[F, Vector[RfAssembly[Int, Int, Int, Int]]])
    : F[Controller[F]] =
    RfAssemblyController
      .make[F, Int, Int, Int, Int](
        RfAssemblyService.make[F, Int, Int, Int, Int](
          RfAssemblyInMemoryRepository.make[F](ref)
        )
      )
      .pure
