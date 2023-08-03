package com.nexters.bandalart.android.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.nexters.bandalart.android.feature.home.HomeRoute

const val HOME_NAVIGATION_ROUTE = "home_route"

@Suppress("unused")
fun NavController.navigateToHome(navOptions: NavOptions? = null) {
  this.navigate(HOME_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(
  navigateToOnBoarding: () -> Unit,
  navigateToComplete: () -> Unit,
  onShowSnackbar: suspend (String) -> Boolean,
) {
  composable(route = HOME_NAVIGATION_ROUTE) {
    HomeRoute(
      navigateToOnBoarding = navigateToOnBoarding,
      navigateToComplete = navigateToComplete,
      onShowSnackbar = onShowSnackbar,
    )
  }
}
