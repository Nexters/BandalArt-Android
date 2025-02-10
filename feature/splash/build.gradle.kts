@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.bandalart.android.feature)
}

android {
    namespace = "com.nexters.bandalart.feature.splash"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementations(
        libs.app.update,
        libs.app.update.ktx,

        libs.lottie.compose,
        libs.timber,
    )
}
