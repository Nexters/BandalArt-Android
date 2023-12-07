@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
  bandalart("android-library")
}

android {
  namespace = "com.nexters.bandalart.android.core.util"

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementations(
    libs.kotlinx.datetime,
    libs.androidx.core,
  )
}
