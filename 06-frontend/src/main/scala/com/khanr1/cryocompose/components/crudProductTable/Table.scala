package com.khanr1
package cryocompose
package components
package crudProductTable

import com.raquo.laminar.api.L.{ *, given }

def mainTable[ProductID, CategoryID, TagID](
  productStream: EventStream[List[Product[ProductID, CategoryID, TagID]]]
) =
  table(
    cls := "table table-striped",
    tableHeader,
    tbody(
      children <-- productStream.split(_.productID)(tableRow[ProductID, CategoryID, TagID])
    ),
  )
