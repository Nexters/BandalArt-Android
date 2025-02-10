@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.bandalart.android.feature)
}

android {
    namespace = "com.nexters.bandalart.feature.complete"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementations(
        libs.lottie.compose,
        libs.timber,
        libs.coil.compose,

        libs.bundles.landscapist,
    )
}
