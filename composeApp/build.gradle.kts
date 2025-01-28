plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidLibrary {
        namespace = "com.nexters.bandalart"
        compileSdk = 34
        minSdk = 28
    }

    val xcfName = "composeAppKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlin.inject.runtime)
                implementation(libs.kotlin.inject.anvil.runtime)
                implementation(libs.kotlin.inject.anvil.runtime.optional)
                implementation(libs.bundles.circuit)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.kotlin.inject.anvil.runtime)
                implementation(libs.kotlin.inject.anvil.runtime.optional)
            }
        }

        iosMain {}
    }
}

ksp {
    // arg("me.tatarka.inject.generateCompanionExtensions", "true")
    arg("circuit.codegen.lenient", "true")
    arg("circuit.codegen.mode", "kotlin_inject_anvil")
    arg("kotlin-inject-anvil-contributing-annotations", "com.slack.circuit.codegen.annotations.CircuitInject")
}

dependencies {
    ksp(libs.androidx.room.compiler)
    ksp(libs.kotlin.inject.compiler)
    ksp(libs.kotlin.inject.anvil.compiler)
    ksp(libs.circuit.codegen)
}
