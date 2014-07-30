name := "play2-chosenlang"

organization := "me.mnedokushev"

version := "1.0-SNAPSHOT"

resolvers += "Sonatype Snapshots"  at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq( 
  cache,
  "jp.t2v" %% "stackable-controller" % "0.4.0"
)     

play.Project.playScalaSettings

scalaSource in Compile := baseDirectory.value / "module"

