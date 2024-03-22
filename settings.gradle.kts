rootProject.name = "Colonel"

includeBuild("./gradle/build-logic")
include(":core")

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

  repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://libraries.minecraft.net")
  }
}