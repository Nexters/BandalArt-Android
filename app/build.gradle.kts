@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

import java.util.Properties

plugins {
  bandalart("android-application")
  bandalart("android-hilt")
  alias(libs.plugins.google.service)
  alias(libs.plugins.firebase.crashlytics)
}

android {
  namespace = "com.nexters.bandalart.android"

  signingConfigs {
    create("release") {
      val propertiesFile = project.file("keystore.properties")
      val properties = Properties()
      properties.load(propertiesFile.inputStream())
      storeFile = file(properties["STORE_FILE"] as String)
      storePassword = properties["STORE_PASSWORD"] as String
      keyAlias = properties["KEY_ALIAS"] as String
      keyPassword = properties["KEY_PASSWORD"] as String
    }
  }

  buildTypes {
    getByName("debug") {
      proguardFiles(
        getDefaultProguardFile("proguard-android.txt"),
        "proguard-debug.pro"
      )
      applicationIdSuffix = ".dev"
      manifestPlaceholders += mapOf(
        "appName" to "@string/app_name_dev"
      )
    }

    getByName("release") {
      isDebuggable = false
      isMinifyEnabled = true
      proguardFiles(
        getDefaultProguardFile("proguard-android.txt"),
        "proguard-rules.pro"
      )
      signingConfig = signingConfigs.getByName("release")
      manifestPlaceholders += mapOf(
        "appName" to "@string/app_name"
      )
    }
  }

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
    projects.core.datastore,
    projects.core.designsystem,
    projects.core.domain,
    projects.core.network,
    projects.core.ui,
    projects.feature.complete,
    projects.feature.home,
    projects.feature.onboarding,
    projects.feature.splash,
    libs.androidx.splash,
    libs.androidx.startup,
    libs.androidx.core,
    libs.androidx.splash,
    libs.timber,
    libs.bundles.androidx.compose,
    platform(libs.firebase.bom),
    libs.firebase.analytics,
    libs.firebase.crashlytics
  )
}
