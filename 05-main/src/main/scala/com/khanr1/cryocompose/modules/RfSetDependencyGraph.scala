package com.khanr1
package cryocompose
package modules

import cats.*
import cats.effect.kernel.Ref
import com.khanr1.cryocompose.wiring.rf.RfSet
import com.khanr1.cryocompose.services.wiring.rf.RfSetService
import com.khanr1.cryocompose.repositories.inMemory.RfSetInMemoryRepository
import com.khanr1.cryocompose.controllers.Controller
import com.khanr1.cryocompose.controllers.inMemory.RfSetController

object RfSetDependencyGraph:
  def make[F[_]: MonadThrow: effect.Async](ref: Ref[F, Vector[RfSet[Int, Int, Int, Int]]])
    : F[Controller[F]] =
    RfSetController
      .make[F, Int, Int, Int, Int](
        RfSetService.make[F, Int, Int, Int, Int](
          RfSetInMemoryRepository.make[F](ref)
        )
      )
      .pure
