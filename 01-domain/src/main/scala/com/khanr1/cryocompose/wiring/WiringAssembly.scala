package com.khanr1
package cryocompose
package wiring

/** Representation of a wiring assembly
  *
  * @param connectors
  *   are the connector in the wiring assembly
  * @param line
  *   describes how the connector are linked to each others.
  */
trait WiringAssembly(connectors: List[Connector], line: List[Line])
