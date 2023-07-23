package com.nexters.bandalart.android.feature.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.nexters.bandalart.android.feature.onboarding.OnBoardingRoute

const val onBoardingNavigationRoute = "onboarding_route"

fun NavController.navigateToOnBoarding(navOptions: NavOptions? = null) {
  this.navigate(onBoardingNavigationRoute, navOptions)
}

fun NavGraphBuilder.onBoardingScreen(onNavigateBack: () -> Unit) {
  composable(route = onBoardingNavigationRoute) {
    OnBoardingRoute(onNavigateBack)
  }
}
