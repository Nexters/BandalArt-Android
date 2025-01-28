@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.bandalart.android.feature)
}

android {
    namespace = "com.nexters.bandalart.feature.onboarding"

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
        projects.feature.home,

        libs.lottie.compose,
        libs.napier,
    )
}
