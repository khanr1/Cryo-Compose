package com.khanr1
package cryocompose
package wiring

import cats.derived.*
import cats.Show
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import io.circe.Encoder
import io.circe.generic.auto.*

import monocle.Iso
import io.circe.Decoder

/** Represents a connector with a name, gender, and number of pins.
  * @param name
  *   The name of the connector.
  * @param gender
  *   The gender of the connector.
  * @param numberPin
  *   The number of pins in the connector.
  */
trait Connector(
  ConnectorName: ConnectorName,
  gender: Gender,
  numberPin: NumberOfPin,
)

/** Represents the name of a connector, constrained to not be an empty string.
  */
type ConnectorNameR =
  DescribedAs[Not[Empty], "The name of a connector cannot be an empty string"]
opaque type ConnectorName = String :| ConnectorNameR
object ConnectorName extends RefinedTypeOps[String, ConnectorNameR, ConnectorName]:
  given show: Show[ConnectorName] = Show.fromToString

/** Represents the number of pins in a connector, constrained to be a positive
  * integer.
  */
type NumberOfPinR =
  DescribedAs[
    Positive,
    "The number of Pin in a connector has to be strictly greater than 0",
  ]
opaque type NumberOfPin = Int :| NumberOfPinR
object NumberOfPin extends RefinedTypeOps[Int, NumberOfPinR, NumberOfPin]:
  given show: Show[NumberOfPin] = Show.fromToString

/** Enumerates the genders of a connector: Male or Female.
  */
enum Gender derives Show:
  case Male
  case Female

object Gender:
  import Gender.*
  given iso: Iso[Gender, Boolean] = Iso[Gender, Boolean] {
    case Female => true
    case Male => false
  }(if _ then Female else Male)
  given Encoder[Gender] = Encoder.forProduct1("gender")(g =>
    g match
      case Male => "Male"
      case Female => "Female"
  )
  given Decoder[Gender] = Decoder.forProduct1("gender")(Gender.valueOf(_))
