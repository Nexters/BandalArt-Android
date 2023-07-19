@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
  bandalart("android-library")
  bandalart("android-hilt")
}

android {
  namespace = "com.nexters.bandalart.android.presentation"

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
    libs.androidx.appcompat,
    libs.androidx.core,
    libs.androidx.hilt.compose.navigation,
    libs.androidx.splash,
    libs.bundles.androidx.compose,
    libs.bundles.androidx.lifecycle,
    projects.domain,
  )
}
