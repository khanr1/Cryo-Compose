package com.khanr1
package cryocompose
package components
package product

import cats.Show
import com.raquo.laminar.api.L.{ *, given }
import com.raquo.laminar.defs.attrs.AriaAttrs
import com.raquo.laminar.keys.AriaAttr
import utils.{ HasHierarchy, Tree }
import org.scalajs.dom.HTMLDivElement
import com.raquo.laminar.nodes.ReactiveHtmlElement

def productCard[ProductID, CategoryID, TagID](product: Product[ProductID, CategoryID, TagID]) =
  div(
    cls := "card",
    styleAttr := "width: 18rem;",
    svg.svg(
      svg.cls := "bd-placeholder-img card-img-top",
      svg.width := "100%",
      svg.height := "225",
      svg.xmlns := "http://www.w3.org/2000/svg",
      svg.role := "img",
      aria.label := "Placeholder: Thumbnail",
      svg.preserveAspectRatio := "xMidYMid slice",
      svg.titleTag("Placeholder"),
      svg.rect(svg.width := "100%", svg.height := "100%", svg.fill := "#55595c"),
      svg.text(svg.x := "50%", svg.y := "50%", svg.fill := "#eceeef", svg.dy := ".3em", "Image"),
    ),
    div(
      cls := "card-body",
      h5(cls := "card-title", product.productName.value),
      h6(cls := "card-subtitle mb-2 text-muted", product.code.value),
      p(cls := "card-text", product.productDescription.value),
    ),
  )
