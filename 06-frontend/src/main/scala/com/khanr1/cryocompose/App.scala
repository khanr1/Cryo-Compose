package com.khanr1
package cryocompose

import org.scalajs.dom
import com.raquo.laminar.api.L.{ *, given }
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
  val test = navTag(
    cls := "navbar navbar-expand-lg navbar-light bg-light",
    a(cls := "navbar-brand", href := "#", "Navbar"),
    button(
      cls := "navbar-toggler",
      tpe := "button",
      dataAttr("data-toggle") := "collapse",
      dataAttr("data-target") := "#navbarSupportedContent",
      dataAttr("aria-controls") := "navbarSupportedContent",
      dataAttr("aria-expanded") := "false",
      dataAttr("aria-label") := "Toggle navigation",
      span(cls := "navbar-toggler-icon"),
    ),
    div(
      cls := "collapse navbar-collapse",
      idAttr := "navbarSupportedContent",
      ul(
        cls := "navbar-nav mr-auto",
        li(
          cls := "nav-item active",
          a(cls := "nav-link", href := "#", "Home", span(cls := "sr-only", "(current)")),
        ),
        li(cls := "nav-item", a(cls := "nav-link", href := "#", "Link")),
        li(
          cls := "nav-item dropdown",
          a(
            cls := "nav-link dropdown-toggle",
            href := "#",
            idAttr := "navbarDropdown",
            role := "button",
            dataAttr("data-toggle") := "dropdown",
            dataAttr("aria-haspopup") := "true",
            dataAttr("aria-expanded") := "false",
            "Dropdown",
          ),
          div(
            cls := "dropdown-menu",
            dataAttr("aria-labelledby") := "navbarDropdown",
            a(cls := "dropdown-item", href := "#", "Action"),
            a(cls := "dropdown-item", href := "#", "Another action"),
            div(cls := "dropdown-divider"),
            a(cls := "dropdown-item", href := "#", "Something else here"),
          ),
        ),
        li(cls := "nav-item", a(cls := "nav-link disabled", href := "#", "Disabled")),
      ),
      form(
        cls := "form-inline my-2 my-lg-0",
        input(
          cls := "form-control mr-sm-2",
          tpe := "search",
          placeholder := "Search",
          dataAttr("aria-label") := "Search",
        ),
        button(cls := "btn btn-outline-success my-2 my-sm-0", tpe := "submit", "Search"),
      ),
    ),
  )

  def main(args: Array[String]): Unit =
    render(containerNode, test)
