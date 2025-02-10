@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.bandalart.android.feature)
}

android {
    namespace = "com.nexters.bandalart.feature.home"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementations(
        libs.kotlinx.collections.immutable,
        libs.kotlinx.datetime,

        libs.androidx.core,

        libs.app.update,
        libs.app.update.ktx,

        libs.lottie.compose,
        libs.facebook.shimmer,
        libs.napier,
    )
}
