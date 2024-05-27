package com.khanr1
package cryocompose
package components
package crudProductTable

import com.raquo.laminar.api.L.{ *, given }

def tableRow[ProductID, CategoryID, TagID](
  productID: ProductID,
  product: Product[ProductID, CategoryID, TagID],
  signal: Signal[Product[ProductID, CategoryID, TagID]],
): HtmlElement =
  tr(
    td(productID.toString()),
    td(child.text <-- signal.map(_.code.value.show)),
    td(child.text <-- signal.map(_.productName.value.show)),
    td(child.text <-- signal.map(_.categoryID.toString())),
    td(child.text <-- signal.map(_.tagsID.mkString("[", ",", "]"))),
    td(
      button(
        typ := "button",
        cls := "btn btn--outline-secondary",
        dataAttr("bs-toggle") := "modal",
        dataAttr("bs-target") := s"#${productID.toString()}",
        i(cls := "bi bi-info-square-fill"),
      )
    ),
    // Modal
    div(
      cls := "modal fade",
      idAttr := productID.toString(),
      tabIndex := -1,
      aria.labelledBy(s"label${productID.toString()}"),
      aria.hidden := true,
      div(
        cls := "modal-dialog",
        cls := "modal-lg",
        div(
          cls := "modal-content",
          div(
            cls := "modal-header",
            h1(
              cls := "modal-title fs-5",
              idAttr := "staticBackdropLabel",
              child.text <-- signal.map(_.productName.toString()),
            ),
            button(
              typ := "button",
              cls := "btn-close",
              dataAttr("bs-dismiss") := "modal",
              aria.label := "Close",
            ),
          ),
          div(
            cls := "modal-body",
            pre(child.text <-- signal.map(_.productDescription.value)),
          ),
          div(
            cls := "modal-footer",
            button(
              tpe := "button",
              cls := "btn btn-secondary",
              dataAttr("bs-dismiss") := "modal",
              "Close",
            ),
          ),
        ),
      ),
    ),
  )
