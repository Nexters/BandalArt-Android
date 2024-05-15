@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
  bandalart("android-library")
  bandalart("android-compose")
}

android {
  namespace = "com.nexters.bandalart.android.core.designsystem"

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementations(
    libs.bundles.androidx.compose
  )
}
