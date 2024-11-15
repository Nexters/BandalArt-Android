@file:Suppress("INLINE_FROM_HIGHER_PLATFORM", "UnstableApiUsage")

plugins {
  alias(libs.plugins.bandalart.android.library)
  alias(libs.plugins.bandalart.android.hilt)
  alias(libs.plugins.bandalart.android.retrofit)
  alias(libs.plugins.kotlin.serialization)
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

    libs.bundles.ktor.client,
    libs.timber,
  )
}
