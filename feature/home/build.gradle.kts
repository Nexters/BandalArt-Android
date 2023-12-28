@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
  bandalart("android-library")
  bandalart("android-compose")
  bandalart("android-hilt")
}

android {
  namespace = "com.nexters.bandalart.android.feature.home"

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  coreLibraryDesugaring(libs.desugar.jdk)
  implementations(
    projects.core.designsystem,
    projects.core.domain,
    projects.core.ui,
    projects.core.util,
    libs.kotlinx.collections.immutable,
    libs.kotlinx.datetime,
    libs.androidx.core,
    libs.androidx.hilt.compose.navigation,
    libs.lottie.compose,
    libs.facebook.shimmer,
    libs.timber,
    libs.bundles.androidx.compose,
    libs.bundles.androidx.lifecycle
  )
}
