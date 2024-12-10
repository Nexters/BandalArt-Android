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
        libs.kotlinx.datetime,

        libs.androidx.core,

        libs.app.update,
        libs.app.update.ktx,

        libs.timber,
        libs.bundles.retrofit,
    )
}
