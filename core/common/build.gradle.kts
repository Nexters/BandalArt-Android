@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
  bandalart("android-library")
  bandalart("android-compose")
}

android {
  namespace = "com.nexters.bandalart.android.core.common"

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementations(
    libs.kotlinx.datetime,
    libs.androidx.core,
    libs.timber,
    libs.bundles.retrofit,
    libs.bundles.androidx.compose,
  )
}
