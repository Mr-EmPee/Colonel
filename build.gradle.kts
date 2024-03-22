plugins {
  id("template.java-conventions")

  id("io.freefair.lombok") version "8.4"
  id("com.github.johnrengelman.shadow") version "8.1.0"
  id("com.vanniktech.maven.publish") version "0.28.0"
}

version = findProperty("tag") ?: "0.0.1-SNAPSHOT"

dependencies {
  implementation(project(":core"))
}

mavenPublishing {
  coordinates("io.github.mr-empee", "colonel", version.toString())

  pom {
    name.set("Colonel")
    description.set("Define commands by using brigadier the official minecraft command framework ")
    inceptionYear.set("2024")
    url.set("https://github.com/Mr-EmPee/Colonel")
    licenses {
      license {
        name.set("The Apache License, Version 2.0")
        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
        distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
      }
    }

    developers {
      developer {
        id.set("mr-empee")
        name.set("Mr. EmPee")
        url.set("https://github.com/mr-empee/")
      }
    }

    scm {
      url.set("https://github.com/Mr-EmPee/Colonel")
      connection.set("scm:git:git://github.com/Mr-EmPee/Colonel.git")
      developerConnection.set("scm:git:ssh://git@github.com:Mr-EmPee/Colonel.git")
    }
  }
}