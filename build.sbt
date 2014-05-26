name := "spellcheck"

version := "1.0"

scalaVersion := "2.10.3"

mainClass in (Compile,run) := Some("com.koadr.Main")

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.3.12" % "test"
)
