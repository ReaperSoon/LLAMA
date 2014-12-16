name := "Llama"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.bouncycastle" % "bcprov-jdk15on" % "1.49",
  "org.bouncycastle" % "bcpkix-jdk15on" % "1.49",
  "be.objectify" %% "deadbolt-java" % "2.2.1-RC2"
)

play.Project.playJavaSettings

resolvers += Resolver.url("Objectify Play Repository", url("http://deadbolt.ws/releases/"))(Resolver.ivyStylePatterns)
