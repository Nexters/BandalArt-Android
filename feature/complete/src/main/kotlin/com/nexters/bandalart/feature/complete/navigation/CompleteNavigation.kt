package com.nexters.bandalart.feature.complete.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.nexters.bandalart.core.navigation.Route
import com.nexters.bandalart.feature.complete.CompleteRoute

fun NavController.navigateToComplete(
    navOptions: NavOptions? = null,
    bandalartId: Long,
    bandalartTitle: String,
    bandalartProfileEmoji: String?,
) {
    this.navigate(Route.Complete(bandalartId, bandalartTitle, bandalartProfileEmoji), navOptions)
}

fun NavGraphBuilder.completeScreen(
    onNavigateBack: () -> Unit,
) {
    composable<Route.Complete> {
        CompleteRoute(onNavigateBack = onNavigateBack)
    }
}
