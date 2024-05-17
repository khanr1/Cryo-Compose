package com.khanr1
package cryocompose

import cats.effect.IO
import cats.effect.unsafe.implicits.*
import com.raquo.laminar.api.L.{ *, given }
import io.circe.syntax.*
import io.circe.parser.decode
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
import com.khanr1.cryocompose.components.product.*
import com.khanr1.cryocompose.components.crudProductTable.*

object App:
  given entityDecoder: EntityDecoder[IO, List[Category[Int]]] =
    jsonOf

  given EntityDecoder[IO, List[RfAssembly[Int, Int, Int, Int]]] =
    jsonOf

  val client = FetchClientBuilder[IO].create
  val containerNode = dom.document.querySelector("#appContainer")

  val rfAssemply = client
    .expect[List[RfAssembly[Int, Int, Int, Int]]](uri"http://localhost:8080/rf/rfassembly")
    .unsafeToFuture()

  val fetchedCategory = FetchStream
    .get("http://localhost:8080/categories")
    .map(response => response.text)
    .map(data => decode[List[Category[Int]]](data))
    .collect {
      case Right(categories) => categories
    }

  def renderCategories =
    div(
      children <-- fetchedCategory.map { categories =>
        categories.map { category =>
          ul(
            li(category.description.value.show)
          )
        }
      }
    )

  val LinkID = Var("")

  def main(args: Array[String]): Unit =
    for
        // c <- categories
        rf <- rfAssemply
    yield render(
      containerNode,
      div(
        child <-- fetchedCategory.map(categories =>
          navigationBar(Tree.construct(categories), LinkID)
        ),
        headerMain("CryoCompose", LinkID),
        mainTable(rf),
        div("hello now it should show that"),
      ),
    )
