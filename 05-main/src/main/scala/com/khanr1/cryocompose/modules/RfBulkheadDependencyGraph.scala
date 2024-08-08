package com.khanr1
package cryocompose
package modules

import cats.*
import cats.effect.Ref
import cats.syntax.all.*

import com.khanr1.cryocompose.wiring.rf.RfBulkhead
import com.khanr1.cryocompose.controllers.inMemory.RfBulkheadController
import com.khanr1.cryocompose.controllers.Controller
import com.khanr1.cryocompose.services.wiring.rf.RfBulkheadService
import com.khanr1.cryocompose.repositories.inMemory.RfBulkheadInMemoryRepository

object RfBulkheadDependencyGraph:
  def make[F[_]: MonadThrow: effect.Async](ref: Ref[F, Vector[RfBulkhead[Int, Int, Int]]])
    : F[Controller[F]] =
    RfBulkheadController
      .make[F, Int, Int, Int](
        RfBulkheadService.make[F, Int, Int, Int](
          RfBulkheadInMemoryRepository.make[F](ref)
        )
      )
      .pure
