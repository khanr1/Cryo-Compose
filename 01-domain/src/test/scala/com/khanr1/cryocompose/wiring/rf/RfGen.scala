package com.khanr1
package cryocompose
package wiring
package rf

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import scala.collection.immutable
import squants.time.Gigahertz

def rfConnectorGen[A: Arbitrary, B: Arbitrary, C: Arbitrary]: Gen[RfConnector[A, B, C]] =
  for
    id <- Arbitrary.arbitrary[A]
    name <- connectorNameGen
    gender <- genderGen
    f <- Gen.posNum[Int]
    categoryID <- Arbitrary.arbitrary[B]
    tags <- Gen.listOf(Arbitrary.arbitrary[C])
  yield RfConnector[A, B, C](id, name, gender, Gigahertz(f), categoryID, Set.from(tags))

def RfBulkheadGen[A: Arbitrary, B: Arbitrary, C: Arbitrary]: Gen[RfBulkhead[A, B, C]] =
  for
    id <- Arbitrary.arbitrary[A]
    connector <- rfConnectorGen[A, B, C]
    length <- lengthGenInmm
    isHermetic <- Gen.oneOf(Hermeticity.values)
    category <- Arbitrary.arbitrary[B]
    tags <- Gen.listOf(Arbitrary.arbitrary[C])
  yield RfBulkhead[A, B, C](id, connector, length, isHermetic, category, Set.from(tags))
