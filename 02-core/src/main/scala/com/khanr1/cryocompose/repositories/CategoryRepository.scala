package com.khanr1.cryocompose.repositories

import com.khanr1.cryocompose.Category
import com.khanr1.cryocompose.CategoryName
import com.khanr1.cryocompose.CategoryParam

trait CategoryRepository[F[_], CategoryID]:
  // find category via its ID
  def readByID(id: CategoryID): F[Option[Category[CategoryID]]]
  // find category via its name
  def readByName(name: CategoryName): F[Option[Category[CategoryID]]]
  // find root categories
  def readRoots: F[Vector[Category[CategoryID]]]
  // find all the categories children
  def readChildren(id: CategoryID): F[Vector[Category[CategoryID]]]
  // find the ancestor
  def readAncestors(id: CategoryID): F[Vector[Category[CategoryID]]]
  // create a category
  def create(category: CategoryParam[CategoryID]): F[CategoryID]
  // delete a category
  def delete(id: CategoryID): F[Unit]
  // update a category
  def update(category: Category[CategoryID]): F[CategoryID]
