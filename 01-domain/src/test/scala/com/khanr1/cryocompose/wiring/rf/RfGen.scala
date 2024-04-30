package com.khanr1
package cryocompose
package wiring
package rf

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import scala.collection.immutable

def rfConnectorGen[A: Arbitrary, B: Arbitrary, C: Arbitrary]: Gen[RfConnector[A, B, C]] =
  for
    id <- Arbitrary.arbitrary[A]
    name <- connectorNameGen
    gender <- genderGen
    categoryID <- Arbitrary.arbitrary[B]
    tags <- Gen.listOf(Arbitrary.arbitrary[C])
  yield RfConnector[A, B, C](id, name, gender, categoryID, Set.from(tags))
