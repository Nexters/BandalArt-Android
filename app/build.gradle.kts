@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
  bandalart("android-application")
  bandalart("android-hilt")
  alias(libs.plugins.google.service)
  alias(libs.plugins.firebase.crashlytics)
}

android {
  namespace = "com.nexters.bandalart.android"

  buildFeatures {
    compose = true
    buildConfig = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
  }
}

dependencies {
  coreLibraryDesugaring(libs.desugar.jdk)
  implementations(
    projects.core.data,
    projects.core.designsystem,
    projects.core.domain,
    projects.core.ui,
    projects.feature.complete,
    projects.feature.home,
    projects.feature.onboarding,
    libs.androidx.splash,
    libs.androidx.startup,
    libs.androidx.appcompat,
    libs.androidx.core,
    libs.androidx.hilt.compose.navigation,
    libs.androidx.splash,
    libs.timber,
    libs.bundles.androidx.compose,
    libs.bundles.androidx.lifecycle,
    platform(libs.firebase.bom),
    libs.firebase.analytics,
    libs.firebase.crashlytics
  )
}
