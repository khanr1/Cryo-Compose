package com.khanr1
package cryocompose
package modules

import cats.*
import cats.effect.Ref
import cats.syntax.all.*

import com.khanr1.cryocompose.controllers.Controller
import com.khanr1.cryocompose.controllers.inMemory.CategoryController
import com.khanr1.cryocompose.repositories.inMemory.CategoryInMemoryRepository
import com.khanr1.cryocompose.services.CategoryService
import cats.effect.kernel.Async

object CategoryDependencyGraph:
  def make[F[_]: MonadThrow: Async](ref: Ref[F, Vector[Category[Int]]]): F[Controller[F]] =
    CategoryController
      .make[F, Int](
        CategoryService.make[F, Int](
          CategoryInMemoryRepository.make[F](ref)
        )
      )
      .pure
