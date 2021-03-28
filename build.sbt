import scalariform.formatter.preferences._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

val baseSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.5"
  ),
  resolvers += Resolver.sonatypeRepo("public")
)

lazy val root = (project in file("."))
  .settings(name := "circle-tank")
  .settings(baseSettings: _*)

val AkkaVersion = "2.6.13"

lazy val core = (project in file("core"))
  .settings(name := "circle-tank-core")
  .settings(baseSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream-typed" % AkkaVersion,
  ))

lazy val viewer = (project in file("viewer"))
  .settings(name := "circle-tank-viewer")
  .settings(baseSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.scala-lang.modules" %% "scala-swing" % "2.1.1"
  ))
  .dependsOn(core)

scalariformPreferences := scalariformPreferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(DanglingCloseParenthesis, Preserve)
