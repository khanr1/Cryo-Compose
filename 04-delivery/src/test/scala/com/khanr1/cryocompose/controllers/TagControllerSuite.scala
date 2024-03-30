package com.khanr1
package cryocompose
package controllers

import cats.effect.{ IO, Ref }
import cats.effect.unsafe.implicits.global
import inMemory.TagController
import io.github.iltotore.iron.autoRefine
import org.http4s.*
import org.http4s.Method.*
import org.http4s.syntax.literals.*
import org.http4s.client.dsl.io.*
import org.scalacheck.Gen
import services.TagService
import weaver.*
import weaver.scalacheck.*
import suite.HttpSuite
import io.github.iltotore.iron.circe.given
import cats.Show

object TagControllerSuite extends HttpSuite:
  given Show[Tag[Int]] = Show.fromToString
  def tagServicesTest(tags: Vector[Tag[Int]]): TestTagServices =
    new TestTagServices:
      override def findAllTag: IO[Vector[Tag[Int]]] = IO.pure(tags)

  test("GET all tags succeeds") {
    forall(Gen.listOf(tagGen[Int]))(tags =>
      val bodyRes = tags.map(response.Tag[Int](_)).toVector
      val req = GET(uri"/tags")
      val routes = TagController.make[IO, Int](tagServicesTest(tags.toVector)).routes
      expectHttpBodyAndStatus(routes, req)(bodyRes, Status.Ok)
    )
  }

protected class TestTagServices extends TagService[IO, Int]:
  override def createTag(name: TagName): IO[Tag[Int]] = ???

  override def findAllTag: IO[Vector[Tag[Int]]] = ???

  override def deleteTag(id: Int): IO[Unit] = ???
