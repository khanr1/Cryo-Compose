package com.khanr1
package cryocompose
package components
package product

import com.raquo.laminar.api.L.{ *, given }

def productCardBoard[ProductID, CategoryID, TagID](
  list: List[Product[ProductID, CategoryID, TagID]]
) =
  div(
    cls := "row row-cols-1 row-cols-md-3 g-4",
    for product <- list
    yield div(cls := "col", productCard(product)),
  )
