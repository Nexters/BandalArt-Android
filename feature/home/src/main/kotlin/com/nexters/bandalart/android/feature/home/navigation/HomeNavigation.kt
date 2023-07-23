package com.nexters.bandalart.android.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.nexters.bandalart.android.feature.home.HomeRoute

const val homeNavigationRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
  this.navigate(homeNavigationRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
  navigateToOnBoarding: () -> Unit,
  navigateToComplete: () -> Unit,
  onShowSnackbar: suspend (String) -> Boolean,
) {
  composable(route = homeNavigationRoute) {
    HomeRoute(
      navigateToOnBoarding = navigateToOnBoarding,
      navigateToComplete = navigateToComplete,
      onShowSnackbar = onShowSnackbar,
    )
  }
}