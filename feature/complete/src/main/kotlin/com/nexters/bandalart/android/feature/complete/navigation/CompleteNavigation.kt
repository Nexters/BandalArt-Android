package com.nexters.bandalart.android.feature.complete.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nexters.bandalart.android.feature.complete.CompleteRoute

const val BANDALART_KEY = "bandalart_key"
const val BANDALART_TITLE = "bandalart_title"
const val BANDALART_PROFILE_EMOJI = "bandalart_profile_image"
const val COMPLETE_NAVIGATION_ROUTE = "complete_route/{$BANDALART_KEY}/{$BANDALART_TITLE}/{$BANDALART_PROFILE_EMOJI}"

fun NavController.navigateToComplete(
  navOptions: NavOptions? = null,
  bandalartKey: String,
  bandalartTitle: String,
  bandalartProfileEmoji: String,
) {
  this.navigate("complete_route/$bandalartKey/$bandalartTitle/$bandalartProfileEmoji", navOptions)
}

fun NavGraphBuilder.completeScreen(
  onNavigateBack: () -> Unit,
  onShowSnackbar: suspend (String) -> Boolean,
) {
  composable(
    route = COMPLETE_NAVIGATION_ROUTE,
    arguments = listOf(
      navArgument(BANDALART_KEY) {
        type = NavType.StringType
      },
      navArgument(BANDALART_TITLE) {
        type = NavType.StringType
      },
      navArgument(BANDALART_PROFILE_EMOJI) {
        type = NavType.StringType
      },
    ),
  ) {
    CompleteRoute(
      onNavigateBack = onNavigateBack,
      onShowSnackbar = onShowSnackbar,
    )
  }
}
