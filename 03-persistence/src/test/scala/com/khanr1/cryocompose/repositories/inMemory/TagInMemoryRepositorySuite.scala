package com.khanr1
package cryocompose
package repositories
package inMemory

import cats.effect.{ IO, Ref }
import weaver.*
import weaver.scalacheck.*
import cats.effect.unsafe.implicits.global
import io.github.iltotore.iron.autoRefine

object TagInMemoryRepositorySuite extends SimpleIOSuite with Checkers:
  // Define a helper method to create an instance of TagRepository for testing
  def createTestRepository(initialState: Vector[Tag[Int]]): IO[TagRepository[IO, Int]] =
    Ref.of[IO, Vector[Tag[Int]]](initialState).map(TagInMemoryRepository.make)

  val TestTags: Vector[Tag[Int]] = Vector(Tag(1, TagName("test")))

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
  test("delete should remove a existing tag to from the repository") {
    for
      repo <- createTestRepository(TestTags)
      _ <- repo.delete(1)
      tags <- repo.findAll()
    yield expect(tags.size == 0)
  }

  test("delete should return an error when the TagID is not in the repo") {
    for
      repo <- createTestRepository(TestTags)
      maybeError <- repo.delete(2).attempt
    yield
      val message = maybeError.left.map(x => x.getMessage())
      expect(message === Left("Failed to delete Tag with id 2 because it didn't exist."))
  }
