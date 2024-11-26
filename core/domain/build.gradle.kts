plugins {
    alias(libs.plugins.bandalart.jvm.kotlin)
    alias(libs.plugins.bandalart.kotest)
}

dependencies {
    compileOnly(
        libs.compose.stable.marker,
    )
    implementation(libs.javax.inject)
}
