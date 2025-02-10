@file:Suppress("INLINE_FROM_HIGHER_PLATFORM", "UnstableApiUsage")

plugins {
    alias(libs.plugins.bandalart.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.nexters.bandalart.core.datastore"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementations(
        libs.kotlinx.serialization.json,
        libs.androidx.datastore.preferences,

        platform(libs.koin.bom),
        libs.koin.core
    )
}
