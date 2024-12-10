@file:Suppress("UnstableApiUsage", "INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.bandalart.android.feature)
}

android {
    namespace = "com.nexters.bandalart.feature.splash"

    defaultConfig {
        buildConfigField("String", "VERSION_CODE", "\"${libs.versions.versionCode.get()}\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {
    implementations(
        libs.app.update,
        libs.app.update.ktx,

        libs.lottie.compose,
        libs.timber,
    )
}
