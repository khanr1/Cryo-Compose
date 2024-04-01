package com.khanr1
package cryocompose

import cats.effect.{ IO, IOApp }
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import cats.effect.*
import io.github.iltotore.iron.autoRefine

object Main extends IOApp.Simple:
  given logger: Logger[IO] = Slf4jLogger.getLogger

  override def run: IO[Unit] = Program.make[IO]
