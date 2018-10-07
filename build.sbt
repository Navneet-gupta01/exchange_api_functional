import Dependencies._

name := "scala_functional_app"

organization := "com.navneetgupta"

scalaVersion := "2.12.6"

version      := "0.1.0"

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-Ypartial-unification",
  "-language:higherKinds"
)

libraryDependencies ++= {
	val akkaVersion = "2.5.13"
	val catVersion = "1.2.0"
	Seq(
		"org.scalaz" %% "scalaz-core" % "7.2.26",
		"org.typelevel" %% "cats-core" % catVersion,
	    "org.typelevel" %% "cats-macros" % catVersion,
	    "org.typelevel" %% "cats-kernel" % catVersion,
	    "org.typelevel" %% "cats-laws" % catVersion,
	    "org.typelevel" %% "cats-free" % catVersion,
	    "org.typelevel" %% "cats-free" % catVersion,
	    "org.typelevel" %% "cats-effect" % "1.0.0-RC2",
	    "org.typelevel" %% "kittens" % "1.1.1",
	    "io.monix" %% "monix" % "3.0.0-RC1",
    	"com.codecommit" %% "shims" % "1.4.0"
	)
}