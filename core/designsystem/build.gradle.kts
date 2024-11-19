@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.bandalart.android.library)
    alias(libs.plugins.bandalart.android.library.compose)
}

android {
    namespace = "com.nexters.bandalart.core.designsystem"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementations(
        libs.androidx.core,
        libs.bundles.androidx.compose,
    )
}
