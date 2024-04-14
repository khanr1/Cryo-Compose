package com.khanr1
package cryocompose
package controllers

import cats.effect.{ IO, Ref }
import cats.effect.unsafe.implicits.global
import cats.Show

import inMemory.TagController
import response.Tag.given
import services.TagService
import suite.HttpSuite

import io.circe.syntax.*
import io.circe.*

import io.github.iltotore.iron.autoRefine
import io.github.iltotore.iron.circe.given

import org.http4s.*
import org.http4s.client.dsl.io.*
import org.http4s.syntax.literals.*
import org.http4s.circe.*

import org.scalacheck.Gen

import weaver.*
import weaver.scalacheck.*

object TagControllerSuite extends HttpSuite:
  given Show[Tag[Int]] = Show.fromToString
  given Encoder[TagName] = Encoder.forProduct1("name")(t => TagName.toString())
  given EntityEncoder[F, TagName] = jsonEncoderOf

  test("GET all tags succeeds") {
    // test Tag service
    def tagServicesTest(tags: Vector[Tag[Int]]): TestTagServices =
      new TestTagServices:
        override def findAllTag: IO[Vector[Tag[Int]]] = IO.pure(tags)

    forall(Gen.listOf(tagGen[Int]))(tags =>
      val bodyRes = tags.map(response.Tag[Int](_)).toVector
      val req = Method.GET(uri"/tags")
      val routes = TagController.make[IO, Int](tagServicesTest(tags.toVector)).routes
      expectHttpBodyAndStatus(routes, req)(bodyRes, Status.Ok)
    )
  }
  test("POST Tag succeeds") {

    /** Fake Tag Service to test  the createTag method
      *
      * @param tag a Tag[Int]
      * @return a new TestTagService
      */
    def tagServiceTest(tag: Tag[Int]): TestTagServices = new TestTagServices:
      override def createTag(name: TagName): IO[Tag[Int]] = IO.pure(tag)

    forall(tagGen[Int]) { t =>
      val input = t.name // request.Tag.Create(t.name)
      val req = Method.POST(input, uri"/tags")
      val routes = TagController.make(tagServiceTest(t)).routes
      val expected = response.Tag[Int](t).asJson
      expectHttpBodyAndStatus(routes, req)(expected, Status.Created)
    }
  }
  test("POST Tag with empty name failed") {
    def tagServiceTest: TestTagServices = new TestTagServices:
      override def createTag(name: TagName): IO[Tag[Int]] = IO.pure(Tag(1, name))

    val input = "\"name\":\"\""
    val route = TagController.make(tagServiceTest).routes
    val req = Method.POST(input, uri"/tags")
    val expected = Status.BadRequest
    expectStatus(route, req)(expected)
  }

/** TestTagService give the bluepring to build testing services for the Tag entity.
  */
protected class TestTagServices extends TagService[IO, Int]:
  override def createTag(name: TagName): IO[Tag[Int]] = ???

  override def findAllTag: IO[Vector[Tag[Int]]] = ???

  override def deleteTag(id: Int): IO[Unit] = ???
