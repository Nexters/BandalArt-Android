@file:Suppress("INLINE_FROM_HIGHER_PLATFORM", "UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  bandalart("android-library")
  bandalart("android-hilt")
  alias(libs.plugins.kotlinx.serialization)
}

android {
  namespace = "com.nexters.bandalart.android.core.datastore"

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementations(
    libs.kotlinx.serialization.json,
    libs.androidx.datastore.preferences,
  )
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.ExperimentalStdlibApi")
  }
}
