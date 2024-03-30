import MyUtil.*
import Dependencies.*

ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.3"

//Clean Architecture Multibuild

lazy val `cryo-compose` =
  project
    .in(file("."))
    .aggregate(domain, core, delivery, persistence, main)
    .settings(
      name := "Cryo-Compose"
    )

//Domain contains our industry specific logic. It be will be application agnostic logic.
//Most of the information there will moslikely be data structure. For example in the banking
//industry, one will have concept such as Account; Saving Account etc... that will
//be modeled in this folder.

lazy val domain =
  project
    .in(file("01-domain"))
    .settings(
      libraryDependencies ++= Seq(
        Library.iron,
        Library.ironScalaC,
        Library.ironCat,
        Library.ironCirce,
        Library.cats,
        Library.kitten,
        Library.monocle,
        Library.squants,
      )
    )
    .settings(testDependencies)

//Core contains our application logic. In our previous example, creating and account
//or more generally CRUD.

lazy val core =
  project
    .in(file("02-core"))
    .dependsOn(domain % Cctt)
    .settings(
      libraryDependencies ++= Seq(
        Library.cats,
        Library.catsEffect,
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
        Library.ironCirce,
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
