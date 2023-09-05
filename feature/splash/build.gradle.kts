@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
  bandalart("android-library")
  bandalart("android-compose")
  bandalart("android-hilt")
}

android {
  namespace = "com.nexters.bandalart.android.feature.splash"
}

dependencies {
  implementations(
    projects.core.designsystem,
    projects.core.domain,
    projects.core.ui,
    libs.androidx.core,
    libs.androidx.hilt.compose.navigation,
    libs.lottie.compose,
    libs.bundles.androidx.compose,
    libs.bundles.androidx.lifecycle,
    libs.timber,
  )
}
