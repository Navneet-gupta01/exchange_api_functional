import Dependencies._

name := "scala_functional_app"

organization := "com.navneetgupta"

scalaVersion := "2.12.6"

version      := "0.1.0"

resolvers += Resolver.sonatypeRepo("releases")
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-Ypartial-unification",
  "-language:higherKinds"
)

val scalaz_mtl = ProjectRef(uri("git://github.com/rabbitonweb/scalaz-mtl.git"), "root")
val zio = ProjectRef(uri("git://github.com/scalaz/scalaz-zio.git"), "coreJVM")

lazy val root = Project("root", file("."))
  .settings(libraryDependencies += "org.scalaz"     %% "scalaz-core" % "7.2.26")
  .settings(libraryDependencies += "io.monix"       %% "monix"       % "3.0.0-RC1")
  .settings(libraryDependencies += "com.codecommit" %% "shims"       % "1.4.0")
  .dependsOn(scalaz_mtl,zio)