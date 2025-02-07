plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.androidx.room)
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
        androidMain {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)

                implementation(libs.kotlin.inject.anvil.runtime)
                implementation(libs.kotlin.inject.anvil.runtime.optional)
            }
        }

        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlin.inject.runtime)
                implementation(libs.kotlin.inject.anvil.runtime)
                implementation(libs.kotlin.inject.anvil.runtime.optional)
                implementation(libs.bundles.circuit)

                api(libs.androidx.datastore)
                api(libs.androidx.datastore.preferences)

                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)
            }
        }

        iosMain {}
    }
}

room {
    schemaDirectory("$projectDir/schemas")
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
