package com.nexters.bandalart.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.nexters.bandalart.core.navigation.Route
import com.nexters.bandalart.feature.complete.navigation.completeScreen
import com.nexters.bandalart.feature.complete.navigation.navigateToComplete
import com.nexters.bandalart.feature.home.navigation.homeScreen
import com.nexters.bandalart.feature.home.navigation.navigateToHome
import com.nexters.bandalart.feature.onboarding.navigation.navigateToOnBoarding
import com.nexters.bandalart.feature.onboarding.navigation.onBoardingScreen
import com.nexters.bandalart.feature.splash.navigation.splashScreen

// TODO Type Safe Navigation migration
@Composable
fun BandalartNavHost(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String) -> Boolean,
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.Splash,
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
