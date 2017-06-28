name := """play-scala-forms-example"""

version := "2.6.x"

scalaVersion := "2.12.2"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= {
  Seq(
    guice,
    "io.dropwizard.metrics" % "metrics-core" % "3.2.2",
    "com.codahale.metrics" % "metrics-graphite" % "3.0.2",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
  )
}