package com.nexters.bandalart.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.nexters.bandalart.android.feature.complete.navigation.completeScreen
import com.nexters.bandalart.android.feature.complete.navigation.navigateToComplete
import com.nexters.bandalart.android.feature.home.navigation.HOME_NAVIGATION_ROUTE
import com.nexters.bandalart.android.feature.home.navigation.homeScreen
import com.nexters.bandalart.android.feature.home.navigation.navigateToHome
import com.nexters.bandalart.android.feature.onboarding.navigation.onBoardingScreen
import com.nexters.bandalart.android.ui.BandalartAppState

@Composable
fun BandalartNavHost(
  modifier: Modifier = Modifier,
  appState: BandalartAppState,
  onShowSnackbar: suspend (String) -> Boolean,
  startDestination: String = HOME_NAVIGATION_ROUTE,
) {
  val navController = appState.navController
  NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = startDestination,
  ) {
    onBoardingScreen(
      navigateToHome = navController::navigateToHome,
    )
    homeScreen(
      navigateToComplete = { key, title, emoji ->
        navController.navigateToComplete(
          bandalartKey = key,
          bandalartTitle = title,
          bandalartProfileEmoji = emoji,
        )
      },
      onShowSnackbar = onShowSnackbar,
    )
    completeScreen(
      onNavigateBack = navController::popBackStack,
      onShowSnackbar = onShowSnackbar,
    )
  }
}
