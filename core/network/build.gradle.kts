@file:Suppress("INLINE_FROM_HIGHER_PLATFORM", "UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  bandalart("android-library")
  bandalart("android-hilt")
  alias(libs.plugins.google.secrets)
  alias(libs.plugins.kotlinx.serialization)
}

android {
  namespace = "com.nexters.bandalart.android.core.network"

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementations(
    projects.core.datastore,

    libs.kotlinx.serialization.json,
    libs.bundles.ktor.client,
    libs.bundles.retrofit,
    libs.bundles.okhttp,
    libs.timber,
  )
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.ExperimentalStdlibApi")
  }
}

secrets {
  defaultPropertiesFileName = "secrets.properties"
}
