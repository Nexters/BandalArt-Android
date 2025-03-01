import com.android.build.gradle.internal.ide.kmp.KotlinAndroidSourceSetMarker.Companion.android
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.androidx.room)
    // alias(libs.plugins.buildkonfig)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    val xcfName = "ComposeApp"

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = xcfName
            isStatic = true
        }
    }

    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.androidx.compose.ui.tooling.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.splash)

                implementation(libs.koin.android)
                // implementation(libs.koin.androidx.startup)
            }
        }

        commonMain {
            dependencies {
                // compileOnly(libs.compose.stable.marker)

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.jetbrains.androidx.navigation.compose)

                api(libs.androidx.datastore)
                api(libs.androidx.datastore.preferences)

                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)

                api(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)

                implementation(libs.napier)
                implementation(libs.uri.kmp)
                implementation(libs.compottie)
                implementation(libs.filekit.core)
                implementation(libs.cmptoast)

                implementation(libs.coil3.compose)
                implementation(libs.landscapist.coil3)
                implementation(libs.landscapist.placeholder)

                implementation(libs.filekit.core)
                implementation(libs.filekit.compose)
            }
        }

        iosMain {
            dependencies {}
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

android {
    namespace = "com.nexters.bandalart"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        create("release") {
            val propertiesFile = rootProject.file("keystore.properties")
            val properties = Properties()
            properties.load(propertiesFile.inputStream())
            storeFile = file(properties["STORE_FILE"] as String)
            storePassword = properties["STORE_PASSWORD"] as String
            keyAlias = properties["KEY_ALIAS"] as String
            keyPassword = properties["KEY_PASSWORD"] as String
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".dev"
            manifestPlaceholders += mapOf(
                "appName" to "@string/app_name_dev",
            )
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders += mapOf(
                "appName" to "@string/app_name",
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
