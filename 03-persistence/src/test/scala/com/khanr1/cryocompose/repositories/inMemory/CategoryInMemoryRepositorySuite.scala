package com.khanr1
package cryocompose
package repositories
package inMemory

import cats.effect.{ IO, Ref }
import weaver.*
import weaver.scalacheck.*
import cats.effect.unsafe.implicits.global
import io.github.iltotore.iron.autoRefine

object CategoryInMemoryRepositorySuite extends SimpleIOSuite with Checkers:
  // Define a helper method to create an instance of TagRepository for testing
  def createTestRepository(initialState: Vector[Category[Int]]): IO[CategoryRepository[IO, Int]] =
    Ref.of[IO, Vector[Category[Int]]](initialState).map(CategoryInMemoryRepository.make(_))

  val testCategory: Vector[Category[Int]] = Vector(
    Category(1, CategoryName("Wiring"), CategoryDescription(" The wirings"), None),
    Category(2, CategoryName("Coax line"), CategoryDescription(" RF lines"), Some(1)),
    Category(3, CategoryName("SMA Line"), CategoryDescription(" SMA wirings"), Some(2)),
    Category(4, CategoryName("DC Lines"), CategoryDescription(" DC wirings"), Some(1)),
    Category(5, CategoryName("System"), CategoryDescription("System"), None),
  )
  val testCategory2: Vector[Category[Int]] = Vector(
    Category(5, CategoryName("System"), CategoryDescription("System"), None)
  )

  test("Find Children succeds") {
    for
      repo <- createTestRepository(testCategory)
      children <- repo.readChildren(1)
    yield expect(
      children.map(_.id) == Vector(2, 4)
    )
  }
  test("Find Roots succeds") {
    for
      repo <- createTestRepository(testCategory)
      ancestors <- repo.readRoots
    yield expect(
      ancestors.map(_.id) == Vector(1, 5)
    )
  }
  test("Find Ancestors succeds") {
    for
      repo <- createTestRepository(testCategory)
      ancestors <- repo.readAncestors(3)
    yield expect(
      ancestors.map(_.id) == Vector(2, 1)
    )
  }
  test("Delete succeds") {
    for
      repo <- createTestRepository(testCategory)
      _ <- repo.delete(3)
      categories <- repo.readAll()
    yield expect(
      categories.map(_.id) == Vector(1, 2, 4, 5)
    )
  }
  test("Create Success") {
    for
      repo <- createTestRepository(testCategory)
      i <- repo.create(CategoryParam(CategoryName("test"), CategoryDescription("test"), Some(1)))
      all <- repo.readAll()
    yield expect(all.size == 6)
  }

  test("Update Success") {
    for
      repo <- createTestRepository(testCategory2)
      _ <- repo.update(Category(5, CategoryName("test"), CategoryDescription("test"), Some(3)))
      all <- repo.readAll()
    yield expect(
      all.map(_.parent).contains(Some(3))
    )
  }
