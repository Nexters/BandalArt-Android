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
    arg("circuit.codegen.mode", "kotlin_inject_anvil")
    arg("kotlin-inject-anvil-contributing-annotations", "com.slack.circuit.codegen.annotations.CircuitInject")
    arg("circuit.codegen.lenient", "true")
}

dependencies {
    implementations(
        projects.feature.complete,

        libs.kotlinx.collections.immutable,
        libs.kotlinx.datetime,

        libs.androidx.activity.compose,
        libs.androidx.core,

        libs.app.update,
        libs.app.update.ktx,

        libs.lottie.compose,
        libs.facebook.shimmer,
        libs.napier,
    )
}
