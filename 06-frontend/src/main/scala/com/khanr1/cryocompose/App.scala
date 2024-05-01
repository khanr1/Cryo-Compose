package com.khanr1
package cryocompose

import cats.effect.IO
import cats.effect.unsafe.implicits.*
import com.raquo.laminar.api.L.{ *, given }
import org.http4s.*
import org.http4s.circe.*
import org.http4s.dom.FetchClientBuilder
import org.http4s.implicits.*
import org.scalajs.dom

import scala.concurrent.Future

import cryocompose.components.navigation.navigationBar
import cryocompose.utils.Tree
import concurrent.ExecutionContext.Implicits.global
import com.khanr1.cryocompose.wiring.rf.RfAssembly
import com.khanr1.cryocompose.components.product.productCard
object App:
  given entityDecoder: EntityDecoder[IO, List[Category[Int]]] =
    jsonOf

  given EntityDecoder[IO, List[RfAssembly[Int, Int, Int, Int]]] =
    jsonOf

  val client = FetchClientBuilder[IO].create
  val containerNode = dom.document.querySelector("#appContainer")
  val categories = client
    .expect[List[Category[Int]]](uri"http://localhost:8080/categories")
    .unsafeToFuture()
  val rfAssemply = client
    .expect[List[RfAssembly[Int, Int, Int, Int]]](uri"http://localhost:8080/rf/rfassembly")
    .unsafeToFuture()

  def main(args: Array[String]): Unit =
    for
      c <- categories
      rf <- rfAssemply
    yield render(containerNode, productCard(rf.head)) // render(containerNode, navigationBar(Tree.construct(c)))
