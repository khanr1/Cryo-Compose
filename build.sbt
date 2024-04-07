import MyUtil.*
import Dependencies.*

import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport.* //needed for scalajs

ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.3"

//Clean Architecture Multibuild

lazy val `cryo-compose` =
  project
    .in(file("."))
    .aggregate(domain.js, domain.jvm, core, delivery, persistence, main, frontend)
    .settings(
      name := "Cryo-Compose"
    )

//Domain contains our industry specific logic. It be will be application agnostic logic.
//Most of the information there will moslikely be data structure. For example in the banking
//industry, one will have concept such as Account; Saving Account etc... that will
//be modeled in this folder.

lazy val domain = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("01-domain"))
  .jvmSettings(
    libraryDependencies ++= Seq(
      Library.iron.value,
      Library.ironScalaC.value,
      Library.ironCat.value,
      Library.ironCirce.value,
      Library.cats.value,
      Library.kitten.value,
      Library.monocle.value,
      Library.squants.value,
    )
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      Library.iron.value,
      Library.ironScalaC.value,
      Library.ironCat.value,
      Library.ironCirce.value,
      Library.cats.value,
      Library.kitten.value,
      Library.monocle.value,
      Library.squants.value,
    )
  )
  .settings(testDependencies)

//Core contains our application logic. In our previous example, creating and account
//or more generally CRUD.

lazy val core =
  project
    .in(file("02-core"))
    .dependsOn(domain.jvm % Cctt)
    .settings(
      libraryDependencies ++= Seq(
        Library.catsEffect
      )
    )

//How our data are stored

lazy val persistence =
  project
    .in(file("03-persistence"))
    .dependsOn(core % Cctt)

//Make sure to translate request model (web request; CLI) into something our core can understand.

lazy val delivery =
  project
    .in(file("04-delivery"))
    .dependsOn(core % Cctt)
    .settings(
      libraryDependencies ++= Seq(
        Library.htt4sCirce,
        Library.htt4sDsl,
        Library.htt4sEmberServer,
        Library.htt4sEmberClient,
        Library.ironCirce.value,
        Library.circe,
        Library.circeGeneric,
        Library.circeParser,
        Library.htt4sCirce,
      )
    )

//Entry point of the application.

lazy val main =
  project
    .in(file("05-main"))
    .dependsOn(delivery % Cctt)
    .dependsOn(persistence % Cctt)
    .settings(
      libraryDependencies ++= Seq(
        Library.log4cats,
        Library.log4catslf4j,
      )
    )
    .settings(testDependencies)

lazy val frontend =
  project
    .in(file("06-frontend"))
    .dependsOn(domain.js)
    .enablePlugins(ScalaJSPlugin)
    .settings(scalaJSUseMainModuleInitializer := true)
    .settings(
      libraryDependencies ++= Seq(
        Library.laminar.value,
        Library.http4sClient.value,
      )
    )
//Some aliases
addCommandAlias("run", "main/run")
addCommandAlias("reStart", "main/reStart")

//Testing library
lazy val testDependencies = Seq(
  testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
  libraryDependencies ++= Seq(
    Library.weaverCats,
    Library.weaverDiscipline,
    Library.weaverScalaCheck,
  ),
)

//path where the generated scalaJS files goes
val jsPath = "04-delivery/src/main/resources"

lazy val fastOptCompileCopy = taskKey[Unit]("")

fastOptCompileCopy := {
  val source = (frontend / Compile / fastOptJS).value.data
  IO.copyFile(
    source,
    baseDirectory.value / jsPath / "frontend.js",
  )
}

lazy val fullOptCompileCopy = taskKey[Unit]("")

fullOptCompileCopy := {
  val source = (frontend / Compile / fullOptJS).value.data
  IO.copyFile(
    source,
    baseDirectory.value / jsPath / "frontend.js",
  )

}
