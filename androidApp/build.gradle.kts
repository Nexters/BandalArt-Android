@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

import java.util.Properties

plugins {
    alias(libs.plugins.bandalart.android.application)
    alias(libs.plugins.bandalart.android.application.compose)
    alias(libs.plugins.bandalart.android.firebase)
}

android {
    namespace = "com.nexters.bandalart"

    signingConfigs {
        create("release") {
            val propertiesFile = rootProject.file("keystore.properties")
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
            isDebuggable = true
            applicationIdSuffix = ".dev"
            manifestPlaceholders += mapOf(
                "appName" to "@string/app_name_dev",
            )
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders += mapOf(
                "appName" to "@string/app_name",
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementations(
        libs.androidx.activity.compose,
        libs.androidx.splash,
        libs.androidx.startup,
        libs.androidx.core,
        libs.androidx.navigation.compose,
        libs.napier,

        platform(libs.koin.bom),
        libs.koin.core,
        libs.koin.androidx.startup,
    )
}
