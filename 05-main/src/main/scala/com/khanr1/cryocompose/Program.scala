package com.khanr1
package cryocompose

import com.khanr1.cryocompose.modules.TagDependencyGraph
import cats.effect.*
import cats.implicits.*
import com.khanr1.cryocompose.controllers.inMemory.TagController
import io.github.iltotore.iron.autoRefine
import org.typelevel.log4cats.Logger

object Program:
  val initialState: Vector[Tag[Int]] = Vector(
    Tag(1, TagName("wiring")),
    Tag(2, TagName("RF")),
  )

  def make[F[_]: Logger: Async]: F[Unit] =
    for
      ref <- Ref.of[F, Vector[Tag[Int]]](initialState)
      tagController <- TagDependencyGraph.make(ref)
      _ <- HttpServer
        .make(
          HttpApi.make(tagController)
        )
        .serve
        .useForever
    yield ()
