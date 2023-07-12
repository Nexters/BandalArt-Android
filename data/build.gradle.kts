@file:Suppress("INLINE_FROM_HIGHER_PLATFORM", "UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  bandalart("android-library")
  bandalart("android-hilt")
  alias(libs.plugins.google.secrets)
}

android {
  namespace = "com.nexters.bandalart.android.data"

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementations(
    projects.domain,
    libs.androidx.datastore.preferences,
    libs.bundles.ktor.client,
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
