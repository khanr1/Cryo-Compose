package com.khanr1
package cryocompose
package controllers

import org.http4s.HttpRoutes

trait Controller[F[_]]:
  def routes: HttpRoutes[F]
