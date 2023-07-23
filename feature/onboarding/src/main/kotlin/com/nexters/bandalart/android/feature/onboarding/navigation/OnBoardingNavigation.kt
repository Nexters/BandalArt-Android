package com.nexters.bandalart.android.feature.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.nexters.bandalart.android.feature.onboarding.OnBoardingRoute

const val ONBOARDING_NAVIGATION_ROUTE = "onboarding_route"

fun NavController.navigateToOnBoarding(navOptions: NavOptions? = null) {
  this.navigate(ONBOARDING_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.onBoardingScreen(onNavigateBack: () -> Unit) {
  composable(route = ONBOARDING_NAVIGATION_ROUTE) {
    OnBoardingRoute(onNavigateBack)
  }
}
