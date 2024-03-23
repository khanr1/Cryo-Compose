import sbt.*

object Dependencies {
  object Version {
    val cats = "2.10.0"
    val catsEffect = "3.5.4"
    val iron = "2.5.0"
    val kitten = "3.3.0"
    val monocle = "3.2.0"
    val skunk = "0.6.3"
    val weaver = "0.8.4"
    val circe = "0.14.6"
    val squants = "1.8.3"

  }
  object Library {
    val cats = "org.typelevel" %% "cats-core" % Version.cats
    val catsEffect = "org.typelevel" %% "cats-effect" % Version.catsEffect
    val circe = "io.circe" %% "circe-core" % Version.circe
    val iron = "io.github.iltotore" %% "iron" % Version.iron
    val ironCat = "io.github.iltotore" %% "iron-cats" % Version.iron
    val ironCirce = "io.github.iltotore" %% "iron-circe" % Version.iron
    val ironScalaC = "io.github.iltotore" %% "iron-scalacheck" % Version.iron
    val ironSkunk = "io.github.iltotore" %% "iron-skunk" % Version.iron
    val kitten = "org.typelevel" %% "kittens" % Version.kitten
    val monocle = "dev.optics" %% "monocle-core" % Version.monocle
    val skunkCirce = "org.tpolecat" %% "skunk-circe" % Version.skunk
    val squants = "org.typelevel" %% "squants" % Version.squants
    val skunkCore = "org.tpolecat" %% "skunk-core" % Version.skunk

    // Testing library
    val weaverCats = "com.disneystreaming" %% "weaver-cats" % Version.weaver
    val weaverDiscipline =
      "com.disneystreaming" %% "weaver-discipline" % Version.weaver
    val weaverScalaCheck =
      "com.disneystreaming" %% "weaver-scalacheck" % Version.weaver
  }
}
