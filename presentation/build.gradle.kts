@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
  bandalart("android-library")
  bandalart("android-hilt")
}

android {
  namespace = "com.nexters.bandalart.presentation"

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
    libs.androidx.core,
    libs.androidx.appcompat,
    libs.androidx.hilt.compose.navigation,
    libs.bundles.androidx.compose,
    libs.bundles.androidx.lifecycle,
    projects.domain,
  )
}
