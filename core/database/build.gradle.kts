plugins {
    alias(libs.plugins.bandalart.android.library)
    alias(libs.plugins.bandalart.android.hilt)
    alias(libs.plugins.bandalart.android.room)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.nexters.bandalart.android.core.database"
}

dependencies {
    implementations(
        libs.timber,
    )
}
