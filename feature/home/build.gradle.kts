@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.bandalart.android.feature)
}

android {
    namespace = "com.nexters.bandalart.android.feature.home"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementations(
        libs.kotlinx.collections.immutable,
        libs.kotlinx.datetime,
        libs.lottie.compose,
        libs.facebook.shimmer,
        libs.timber,
    )
}
