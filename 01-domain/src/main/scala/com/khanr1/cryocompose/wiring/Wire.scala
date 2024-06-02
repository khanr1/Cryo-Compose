package com.khanr1
package cryocompose
package wiring

import com.khanr1.cryocompose.stages.StageLength

trait Material

/** A trait representing a wire used in electrical connections.
  *
  * @param material the material used for the wire.
  * @param length the length of the wire, specified either as a direct length or as a standard stage length.
  *               If specified as a direct length, it should be of type [[Length]], whereas if specified as a stage length,
  *               it should be of type [[StageLength]].
  */
trait Wire:
  val material: Material
  val length: Length

  def wireCode = s"${material.toString()}-${length}"
