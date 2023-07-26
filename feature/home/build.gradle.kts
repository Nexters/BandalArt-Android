@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
  bandalart("android-library")
  bandalart("android-hilt")
}

android {
  namespace = "com.nexters.bandalart.android.feature.home"

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
    projects.core.domain,
    projects.core.ui,
    libs.androidx.core,
    libs.androidx.hilt.compose.navigation,
    libs.bundles.androidx.compose,
    libs.bundles.androidx.lifecycle
  )
}
