package com.khanr1
package cryocompose
package wiring

import org.scalacheck.Gen
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

val connectorNameGen: Gen[ConnectorName] = nesGen(ConnectorName.applyUnsafe(_))
val numberOfPinGen: Gen[NumberOfPin] =
  Gen.posNum[Int].map(i => i + 1).map(NumberOfPin.applyUnsafe(_))
val genderGen: Gen[Gender] = Gen.oneOf(Gender.values)
/* val connectorGen: Gen[Connector] = for {
  n <- connectorNameGen
  g <- genderGen
  i <- numberOfPinGen
} yield Connector(n, g, i) */
