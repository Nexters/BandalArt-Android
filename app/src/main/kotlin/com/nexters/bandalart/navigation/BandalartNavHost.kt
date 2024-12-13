package com.nexters.bandalart.navigation

//@Composable
//fun BandalartNavHost(
//    modifier: Modifier = Modifier,
//    onShowSnackbar: suspend (String) -> Boolean,
//) {
//    val navController = rememberNavController()
//
//    NavHost(
//        modifier = modifier,
//        navController = navController,
//        startDestination = Route.Splash,
//    ) {
//        splashScreen(
//            navigateToOnBoarding = navController::navigateToOnBoarding,
//            navigateToHome = navController::navigateToHome,
//        )
//        onBoardingScreen(
//            navigateToHome = navController::navigateToHome,
//        )
//        homeScreen(
//            navigateToComplete = { id, title, emoji, imageUri ->
//                navController.navigateToComplete(
//                    bandalartId = id,
//                    bandalartTitle = title,
//                    bandalartProfileEmoji = emoji,
//                    bandalartChartImageUri = imageUri,
//                )
//            },
//            onShowSnackbar = onShowSnackbar,
//        )
//        completeScreen(
//            onNavigateBack = navController::popBackStack,
//        )
//    }
//}
