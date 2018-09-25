
name := "scala-graal"
scalaVersion := "2.12.6"
crossScalaVersions := Seq("2.11.11", "2.12.6")

version := "1.0.0-SNAPSHOT"

enablePlugins(JmhPlugin)

//libraryDependencies += "com.twitter" %% "util-core" % "18.6.0" //from "file:///Users/fbrasil/workspace/util/util-core/target/scala-2.11/util-core-assembly-18.6.0.jar"

libraryDependencies += "com.twitter" %% "finagle-http" % "18.9.0"  % "compile"

assemblyMergeStrategy in assembly := {
  _ match {
	  case "META-INF/MANIFEST.MF" => MergeStrategy.discard
	  case _ => MergeStrategy.first
  }
}