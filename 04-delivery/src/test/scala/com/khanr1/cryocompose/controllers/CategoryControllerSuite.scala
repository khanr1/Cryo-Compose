package com.khanr1
package cryocompose
package controllers

import cats.effect.{ IO, Ref }
import cats.effect.unsafe.implicits.global
import cats.Show

import inMemory.CategoryController
import response.Category.given
import services.CategoryService
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

object CategoryControllerSuite extends HttpSuite:
  given Show[Category[Int]] = Show.fromToString
  test("GET all Categories works") {
    def categoryServiceTest(categories: Vector[Category[Int]]): CategoryTestService =
      new CategoryTestService:
        override def findAll: IO[Vector[Category[Int]]] = IO.pure(categories)

    forall(Gen.listOf(categoryGen[Int]))(categories =>
      val bodyRes = categories.map(response.Category.create(_))
      val req = Method.GET(uri"/categories")
      val routes = CategoryController.make[IO, Int](categoryServiceTest(categories.toVector)).routes
      expectHttpBodyAndStatus(routes, req)(bodyRes, Status.Ok)
    )

  }
  protected class CategoryTestService extends CategoryService[IO, Int]:
    override def findByID(id: Int): IO[Option[Category[Int]]] = ???

    override def updateCategory(category: Category[Int]): IO[Int] = ???

    override def findAncetors(id: Int): IO[Vector[Category[Int]]] = ???

    override def findAll: IO[Vector[Category[Int]]] = ???

    override def findChildren(id: Int): IO[Vector[Category[Int]]] = ???

    override def findByName(name: CategoryName): IO[Option[Category[Int]]] = ???

    override def createCategory(category: CategoryParam[Int]): IO[Category[Int]] = ???

    override def deleteCatgory(id: Int): IO[Unit] = ???

    override def findRoot: IO[Vector[Category[Int]]] = ???
