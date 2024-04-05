package com.khanr1
package cryocompose

import com.khanr1.cryocompose.modules.TagDependencyGraph
import cats.effect.*
import cats.implicits.*
import com.khanr1.cryocompose.controllers.inMemory.TagController
import io.github.iltotore.iron.autoRefine
import org.typelevel.log4cats.Logger
import com.khanr1.cryocompose.modules.CategoryDependencyGraph

object Program:
  val tagState: Vector[Tag[Int]] = Vector(
    Tag(1, TagName("wiring")),
    Tag(2, TagName("RF")),
  )

  val catState: Vector[Category[Int]] = Vector(
    Category(
      1,
      CategoryName("Wiring"),
      CategoryDescription("This category regroup all the wiring"),
      None,
    ),
    Category(
      2,
      CategoryName("RF Wiring"),
      CategoryDescription("This category regroup all the RF lines"),
      Some(1),
    ),
    Category(
      3,
      CategoryName("DC Wiring"),
      CategoryDescription("This category regroup all the DC lines"),
      Some(1),
    ),
  )

  def make[F[_]: Logger: Async]: F[Unit] =
    for
      tagRef <- Ref.of[F, Vector[Tag[Int]]](tagState)
      catRef <- Ref.of[F, Vector[Category[Int]]](catState)
      tagController <- TagDependencyGraph.make(tagRef)
      categoryController <- CategoryDependencyGraph.make(catRef)
      _ <- HttpServer
        .make(
          HttpApi.make(tagController, categoryController)
        )
        .serve
        .useForever
    yield ()
