package com.khanr1
package cryocompose
package wiring

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

/** Represents a line connecting pins from different connectors.
  * @param from
  *   The starting point of the line, consisting of a connector and a pin index.
  * @param to
  *   The ending point of the line, consisting of a connector and a pin index.
  * @param wire
  *   The wire used for the connection.
  */
trait Line:
  val from: (Connector, PinIndex)
  val to: (Connector, PinIndex)
  val wire: Wire

  def lineCode: String = s"${from._1.connectorCode}-${to._1.connectorCode}-${wire.wireCode}"

/** Represents the index of a pin, constrained to be a positive integer.
  */
type PinIndexR =
  DescribedAs[Positive, "The index of a pin is a positive number"]
opaque type PinIndex = Int :| PinIndexR
object PinIndex extends RefinedTypeOps[Int, PinIndexR, PinIndex]
