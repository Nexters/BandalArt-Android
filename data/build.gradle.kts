@file:Suppress("INLINE_FROM_HIGHER_PLATFORM", "UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  bandalart("android-library")
  bandalart("android-hilt")
}

android {
  namespace = "com.nexters.bandalart.data"

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementations(
    libs.timber,
    libs.bundles.ktor.client,
    projects.domain,
  )
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.ExperimentalStdlibApi")
  }
}
