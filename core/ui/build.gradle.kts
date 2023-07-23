@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
  bandalart("android-library")
}

android {
  namespace = "com.nexters.bandalart.android.core.ui"

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
    libs.androidx.core,
    libs.androidx.appcompat,
    libs.android.material,
    libs.bundles.androidx.compose,
  )
}