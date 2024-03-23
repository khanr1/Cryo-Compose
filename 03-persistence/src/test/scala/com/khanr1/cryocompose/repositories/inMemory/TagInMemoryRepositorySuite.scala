package com.khanr1
package cryocompose
package repositories
package inMemory

import cats.effect.{ IO, Ref }
import weaver.*
import weaver.scalacheck.*
import cats.effect.unsafe.implicits.global

object TagInMemoryRepositorySuite extends SimpleIOSuite with Checkers:
  // Define a helper method to create an instance of TagRepository for testing
  def createTestRepository(initialState: Vector[Tag[Int]]): IO[TagRepository[IO, Int]] =
    Ref.of[IO, Vector[Tag[Int]]](initialState).map(TagInMemoryRepository.make)

  // Test case for create method
  test("create should add a new tag to the repository") {
    forall(tagNameGen) { name =>
      for
        repo <- createTestRepository(Vector.empty)
        createdTag <- repo.create(name)
        allTags <- repo.findAll()
      yield expect.all(
        createdTag.name == name,
        allTags.size == 1,
        allTags.headOption.contains(createdTag),
      )
    }
  }
