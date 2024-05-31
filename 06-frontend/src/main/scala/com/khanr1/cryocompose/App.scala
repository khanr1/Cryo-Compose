package com.khanr1
package cryocompose

import com.raquo.laminar.api.L.{ *, given }
import cats.effect.IO
import cats.effect.unsafe.implicits.*
import org.scalajs.dom

import scala.concurrent.Future

import cryocompose.components.navigation.navigationBar
import cryocompose.utils.Tree
import concurrent.ExecutionContext.Implicits.global
import com.khanr1.cryocompose.wiring.rf.RfAssembly
import com.khanr1.cryocompose.components.product.*
import com.khanr1.cryocompose.components.crudProductTable.*
import com.raquo.airstream.state.StrictSignal

object App:
  val containerNode = dom.document.querySelector("#appContainer")

  val LinkID = Var("")
  def productStream = LinkID
    .signal
    .flatMap(x =>
      x match
        case "RF Wiring" => fetchedRfAssembly
        case "RF Sets" => fetchedRfSet
        case _ => EventStream.fromValue(Nil)
    )

  val ps = fetchedRfSet

  // new EventBus[List[Product[Int, Int, Int]]]

  def main(args: Array[String]): Unit =
    render(
      containerNode,
      div(
        child <-- fetchedCategory.map(categories =>
          navigationBar(Tree.construct(categories), LinkID)
        ),
        headerMain("CryoCompose", LinkID),
        mainTable(productStream),
      ),
    )
