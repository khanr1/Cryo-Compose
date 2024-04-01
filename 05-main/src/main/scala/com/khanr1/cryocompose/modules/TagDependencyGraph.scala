package com.khanr1
package cryocompose

package modules

import cats.effect.kernel.Async
import cats.effect.kernel.Ref
import cats.MonadThrow
import com.khanr1.cryocompose.repositories.inMemory.TagInMemoryRepository
import controllers.Controller
import controllers.inMemory.TagController
import cryocompose.services.TagService

object TagDependencyGraph:
  def make[F[_]: Async: MonadThrow](ref: Ref[F, Vector[Tag[Int]]]): F[Controller[F]] =
    TagController
      .make[F, Int](
        TagService.make[F, Int](
          TagInMemoryRepository.make(ref)
        )
      )
      .pure
