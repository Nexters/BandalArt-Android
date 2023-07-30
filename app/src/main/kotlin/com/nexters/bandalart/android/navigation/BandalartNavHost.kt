package com.nexters.bandalart.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.nexters.bandalart.android.feature.complete.navigation.completeScreen
import com.nexters.bandalart.android.feature.complete.navigation.navigateToComplete
import com.nexters.bandalart.android.feature.home.navigation.HOME_NAVIGATION_ROUTE
import com.nexters.bandalart.android.feature.home.navigation.homeScreen
import com.nexters.bandalart.android.feature.onboarding.navigation.navigateToOnBoarding
import com.nexters.bandalart.android.feature.onboarding.navigation.onBoardingScreen
import com.nexters.bandalart.android.ui.BandalartAppState

@Composable
fun BandalartNavHost(
  appState: BandalartAppState,
  onShowSnackbar: suspend (String) -> Boolean,
  modifier: Modifier = Modifier,
  startDestination: String = HOME_NAVIGATION_ROUTE,
) {
  val navController = appState.navController
  NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = startDestination,
  ) {
    homeScreen(
      navigateToOnBoarding = navController::navigateToOnBoarding,
      navigateToComplete = navController::navigateToComplete,
      onAddBandalart = {},
      onShowBandalartList = {},
      onShowSnackbar = onShowSnackbar,
    )
    onBoardingScreen(
      onNavigateBack = navController::popBackStack,
    )
    completeScreen(
      onNavigateBack = navController::popBackStack,
    )
  }
}
