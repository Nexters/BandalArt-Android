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

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {
    implementations(
        projects.feature.complete,

        libs.kotlinx.collections.immutable,
        libs.kotlinx.datetime,

        libs.androidx.core,

        libs.app.update,
        libs.app.update.ktx,

        libs.lottie.compose,
        libs.facebook.shimmer,
        libs.timber,
    )
}
