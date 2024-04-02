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
            else x.map(t => t.id).max
          )
      override def readChildren(id: Int): F[Vector[Category[Int]]] =
        state.get.map(categories => categories.filter(_.parent == Some(id)))

      override def readAncestors(id: Int): F[Vector[Category[Int]]] = ???

      override def delete(id: Int): F[Unit] = ???

      override def readByID(id: Int): F[Option[Category[Int]]] =
        state.get.map(x => x.find(_.id == id))

      override def create(category: CategoryParam[Int]): F[Int] = ???

      override def readRoots: F[Vector[Category[Int]]] =
        state.get.map(categories => categories.filter(_.parent == None))

      override def readByName(name: CategoryName): F[Option[Category[Int]]] = ???

      override def update(category: Category[Int]): F[Int] = ???
