@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.bandalart.jvm.kotlin)
    alias(libs.plugins.bandalart.kotest)
}

dependencies {
    compileOnly(
        libs.compose.stable.marker,
    )
    implementations(
        libs.kotlinx.coroutines.core,
    )
}
