@file:Suppress("INLINE_FROM_HIGHER_PLATFORM", "UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  bandalart("android-library")
  bandalart("android-hilt")
  alias(libs.plugins.kotlinx.serialization)
}

android {
  namespace = "com.nexters.bandalart.android.core.data"

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementations(
    projects.core.domain,
    projects.core.datastore,
    projects.core.network,
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
