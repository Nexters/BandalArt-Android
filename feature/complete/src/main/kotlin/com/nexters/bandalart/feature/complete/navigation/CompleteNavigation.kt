package com.nexters.bandalart.feature.complete.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nexters.bandalart.feature.complete.CompleteRoute

const val BANDALART_ID = "bandalart_key"
const val BANDALART_TITLE = "bandalart_title"
const val BANDALART_PROFILE_EMOJI = "bandalart_profile_image"
const val COMPLETE_NAVIGATION_ROUTE = "complete_route/{$BANDALART_ID}/{$BANDALART_TITLE}/{$BANDALART_PROFILE_EMOJI}"

fun NavController.navigateToComplete(
    navOptions: NavOptions? = null,
    bandalartId: Long,
    bandalartTitle: String,
    bandalartProfileEmoji: String?,
) {
    this.navigate("complete_route/$bandalartId/$bandalartTitle/$bandalartProfileEmoji", navOptions)
}

fun NavGraphBuilder.completeScreen(
    onNavigateBack: () -> Unit,
) {
    composable(
        route = COMPLETE_NAVIGATION_ROUTE,
        arguments = listOf(
            navArgument(BANDALART_ID) {
                type = NavType.LongType
            },
            navArgument(BANDALART_TITLE) {
                type = NavType.StringType
            },
            navArgument(BANDALART_PROFILE_EMOJI) {
                type = NavType.StringType
            },
        ),
    ) {
        CompleteRoute(onNavigateBack = onNavigateBack)
    }
}
