@file:Suppress("INLINE_FROM_HIGHER_PLATFORM", "UnstableApiUsage")

plugins {
    alias(libs.plugins.bandalart.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.nexters.bandalart.core.data"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementations(
        projects.core.domain,
        projects.core.datastore,
        projects.core.database,

        libs.kotlinx.coroutines.core,

        libs.timber,
        platform(libs.koin.bom),
        libs.koin.core,
    )
}
