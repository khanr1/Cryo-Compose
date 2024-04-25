package com.khanr1
package cryocompose
package wiring

/** A trait representing a wiring assembly, which consists of connectors and lines connecting them.
  *
  * @param connectors a list of connectors included in the wiring assembly.
  * @param lines a list of lines representing the connections between connectors in the wiring assembly.
  */
trait WiringAssembly(connectors: List[Connector], lines: List[Line])
