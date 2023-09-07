@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
  bandalart("android-library")
  bandalart("android-compose")
}

android {
  namespace = "com.nexters.bandalart.android.feature.onboarding"

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementations(
    projects.core.designsystem,
    projects.core.domain,
    projects.core.ui,
    libs.androidx.core,
    libs.androidx.hilt.compose.navigation,
    libs.lottie.compose,
    libs.timber,
    libs.bundles.androidx.compose,
    libs.bundles.androidx.lifecycle,
  )
}
