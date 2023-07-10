@file:Suppress("UnstableApiUsage")

rootProject.name = "build-logic"

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../gradle/libs.versions.toml"))
    }
  }
}
