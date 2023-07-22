@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
  bandalart("android-library")
}

android {
  namespace = "com.nexters.designsystem"

  buildFeatures {
    compose = true
    buildConfig = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
  }
}

dependencies {

  implementation(libs.androidx.core)
  implementation(libs.androidx.appcompat)
  implementation(libs.android.material)
  implementation(libs.bundles.androidx.compose)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)
}
