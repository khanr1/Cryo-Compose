package com.khanr1
package cryocompose
package wiring

import cats.derived.*
import cats.Show
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import com.khanr1.cryocompose.ports.Ports
import com.khanr1.cryocompose.stages.Stages

/** Installation flange are installed in a K40 flange. */

trait InstallationFlange:
  val port: Ports
  val stage: Stages
  val numberSlot: NumberOfSlot

type NumberOfSlotR =
  DescribedAs[Positive, "The number of slot in a installation flange need to be positive"]
opaque type NumberOfSlot = Int :| NumberOfSlotR
object NumberOfSlot extends RefinedTypeOps[Int, NumberOfSlotR, NumberOfSlot]:
  given show: Show[NumberOfSlot] = Show.fromToString
