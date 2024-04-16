package com.khanr1
package cryocompose

import org.scalajs.dom
import com.raquo.laminar.api.L.*
import io.circe.*
import io.circe.syntax.*

import org.scalajs.dom.Fetch
import cats.effect.IO
import org.scalajs.dom.ext.Ajax
import concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure

import com.khanr1.cryocompose.utils.Tree

object App:
  val containerNode = dom.document.querySelector("#appContainer")

  def main(args: Array[String]): Unit =
    render(containerNode, div("Hello"))
