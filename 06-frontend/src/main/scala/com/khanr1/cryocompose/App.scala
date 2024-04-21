package com.khanr1
package cryocompose

import org.scalajs.dom
import com.raquo.laminar.api.L.{ *, given }

import com.khanr1.cryocompose.utils.Tree
import com.khanr1.cryocompose.components.navigation.navigationBar

object App:
  val containerNode = dom.document.querySelector("#appContainer")

  val catState: Vector[Category[Int]] = Vector(
    Category(
      1,
      CategoryName.assume("Wiring"),
      CategoryDescription.assume("This category regroup all the wiring"),
      None,
    ),
    Category(
      2,
      CategoryName.assume("RF Wiring"),
      CategoryDescription.assume("This category regroup all the RF lines"),
      Some(1),
    ),
    Category(
      3,
      CategoryName.assume("DC Wiring"),
      CategoryDescription.assume("This category regroup all the DC lines"),
      Some(1),
    ),
  )
  val tree = Tree.construct(catState.toList)
  val categories = navigationBar(tree)
  def main(args: Array[String]): Unit =
    render(containerNode, categories)
