@file:Suppress("INLINE_FROM_HIGHER_PLATFORM", "UnstableApiUsage")

plugins {
  bandalart("android-library")
  bandalart("android-compose")
}

android {
  namespace = "com.nexters.bandalart.android.core.ui"

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementations(
    projects.core.designsystem,
    projects.core.util,
    libs.kotlinx.datetime,
    libs.androidx.core,
    libs.lottie.compose,
    libs.bundles.androidx.compose,
  )
}
