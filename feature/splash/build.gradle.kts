@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
  bandalart("android-library")
}

android {
  namespace = "com.nexters.bandalart.android.feature.splash"

  buildFeatures {
    compose = true
    buildConfig = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
  }
}

dependencies {
  implementations(
    projects.core.designsystem,
    projects.core.ui,
    libs.androidx.core,
    libs.androidx.hilt.compose.navigation,
    libs.lottie.compose,
    libs.bundles.androidx.compose,
    libs.bundles.androidx.lifecycle,
  )
}