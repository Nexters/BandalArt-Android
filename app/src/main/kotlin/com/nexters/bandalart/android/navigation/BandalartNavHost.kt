package com.nexters.bandalart.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.nexters.bandalart.android.feature.complete.navigation.completeScreen
import com.nexters.bandalart.android.feature.complete.navigation.navigateToComplete
import com.nexters.bandalart.android.feature.home.navigation.homeScreen
import com.nexters.bandalart.android.feature.home.navigation.navigateToHome
import com.nexters.bandalart.android.feature.onboarding.navigation.navigateToOnBoarding
import com.nexters.bandalart.android.feature.onboarding.navigation.onBoardingScreen
import com.nexters.bandalart.android.feature.splash.navigation.SPLASH_NAVIGATION_ROUTE
import com.nexters.bandalart.android.feature.splash.navigation.splashScreen
import com.nexters.bandalart.android.ui.BandalartAppState

@Composable
fun BandalartNavHost(
  modifier: Modifier = Modifier,
  appState: BandalartAppState,
  onShowSnackbar: suspend (String) -> Boolean,
) {
  val navController = appState.navController
  NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = SPLASH_NAVIGATION_ROUTE,
  ) {
    splashScreen(
      navigateToOnBoarding = navController::navigateToOnBoarding,
      navigateToHome = navController::navigateToHome,
    )
    onBoardingScreen(
      navigateToHome = navController::navigateToHome,
    )
    homeScreen(
      navigateToComplete = { id, title, emoji ->
        navController.navigateToComplete(
          bandalartId = id,
          bandalartTitle = title,
          bandalartProfileEmoji = emoji,
        )
      },
      onShowSnackbar = onShowSnackbar,
    )
    completeScreen(
      onNavigateBack = navController::popBackStack,
    )
  }
}
