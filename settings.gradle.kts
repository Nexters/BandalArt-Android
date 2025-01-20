@file:Suppress("UnstableApiUsage")

include(":composeApp")


rootProject.name = "bandalart-android"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("build-logic")
}

buildCache {
    local {
        removeUnusedEntriesAfterDays = 7
    }
}

include(
    ":app",

    ":core:data",
    ":core:datastore",
    ":core:database",
    ":core:designsystem",
    ":core:domain",
    ":core:ui",
    ":core:common",

    ":feature:complete",
    ":feature:home",
    ":feature:onboarding",
    ":feature:splash",
)
