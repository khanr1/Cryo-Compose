package com.khanr1
package cryocompose
package utils

import cats.Show
import cats.derived.*

trait Material

enum RFmaterial extends Material derives Show:
  case SCuNi
  case NbTi

object RFmaterial:
  given show: Show[RFmaterial] = Show.fromToString

enum DCmaterial extends Material derives Show:
  case PhBr
  case Cu
