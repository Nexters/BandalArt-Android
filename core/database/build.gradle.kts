plugins {
    alias(libs.plugins.bandalart.android.library)
    alias(libs.plugins.bandalart.kotlin.inject)
    alias(libs.plugins.bandalart.android.room)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.nexters.bandalart.core.database"
}

dependencies {
    implementation(libs.napier)
}
