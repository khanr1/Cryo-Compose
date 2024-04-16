package com.khanr1
package cryocompose
package repositories
package inMemory

import cats.effect.Ref
import cats.*
import cats.syntax.all.*
import cats.implicits.*
import scala.annotation.tailrec

object CategoryInMemoryRepository:
  def make[F[_]: MonadThrow](state: Ref[F, Vector[Category[Int]]]): CategoryRepository[F, Int] =
    new CategoryRepository[F, Int]:
      private val nextInt: F[Int] =
        state
          .get
          .map(x =>
            if x.isEmpty then 1
            else x.map(t => t.id).max + 1
          )
      override def readChildren(id: Int): F[Vector[Category[Int]]] =
        state.get.map(categories => categories.filter(_.parent == Some(id)))

      override def readAncestors(id: Int): F[Vector[Category[Int]]] =
        def ancestor(categories: Vector[Category[Int]], i: Int): Vector[Category[Int]] =
          categories
            .filter(_.id == i)
            .flatMap(c =>
              c.parent match
                case None => Vector.empty
                case Some(value) => categories.filter(value == _.id) ++ ancestor(categories, value)
            )

        state.get.map(categories => ancestor(categories, id))

      override def delete(id: Int): F[Unit] = state.get.flatMap { categories =>
        if categories.exists(category => category.id === id) then
          state.update(x => x.filterNot(_.id == id))
        else
          MonadThrow[F]
            .raiseError(
              throw new java.lang.RuntimeException(
                s"Failed to delete category with id ${id} because it didn't exist."
              )
            )

      }

      override def readByID(id: Int): F[Option[Category[Int]]] =
        state.get.map(x => x.find(_.id == id))

      override def create(category: CategoryParam[Int]): F[Category[Int]] = nextInt
        .map(
          Category(_, category.name, category.description, category.parent)
        )
        .flatMap(category => state.modify(s => (s :+ category) -> category))

      override def readRoots: F[Vector[Category[Int]]] =
        state.get.map(categories => categories.filter(_.parent == None))

      override def readByName(name: CategoryName): F[Option[Category[Int]]] =
        state.get.map(x => x.find(_.name == name))

      override def update(category: Category[Int]): F[Int] = state
        .get
        .flatMap(s =>
          if s.exists(_.id == category.id) then
            state.modify(s => (s.filterNot(_.id == category.id) :+ category) -> category.id)
          else
            MonadThrow[F].raiseError(
              throw new java.lang.RuntimeException(
                s"Failed to update category id ${0}  it doesn't exist."
              )
            )
        )

      override def readAll(): F[Vector[Category[Int]]] = state.get
