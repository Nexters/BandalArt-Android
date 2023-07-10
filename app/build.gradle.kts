@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
  bandalart("android-application")
  bandalart("android-hilt")
}

android {
  namespace = "com.nexters.bandalart.android"

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  coreLibraryDesugaring(libs.desugar.jdk)
  implementations(
    libs.androidx.splash,
    libs.androidx.startup,
    libs.timber,
    projects.data,
    projects.domain,
    projects.presentation,
  )
}
