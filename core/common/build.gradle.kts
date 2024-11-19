@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.bandalart.android.library)
    alias(libs.plugins.bandalart.android.library.compose)
}

android {
    namespace = "com.nexters.bandalart.core.common"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementations(
        libs.androidx.core,
        libs.kotlinx.datetime,
        libs.timber,
        libs.bundles.retrofit,
    )
}
