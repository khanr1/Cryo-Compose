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
    img(src := "...", cls := "card-img-top", alt := "..."),
    div(
      cls := "card-body",
      h5(cls := "card-title", product.productName.value),
      h6(cls := "card-subtitle mb-2 text-muted", product.code.value),
      p(cls := "card-text", product.productDescription.value),
    ),
  )
