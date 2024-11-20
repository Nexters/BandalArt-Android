package com.nexters.bandalart.core.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Splash: Route

    @Serializable
    data object Onboarding: Route

    @Serializable
    data object Home: Route

    @Serializable
    data object Complete: Route
}
