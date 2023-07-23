package com.nexters.bandalart.android.feature.complete.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.nexters.bandalart.android.feature.complete.CompleteRoute

const val COMPLETE_NAVIGATION_ROUTE = "complete_route"

fun NavController.navigateToComplete(navOptions: NavOptions? = null) {
  this.navigate(COMPLETE_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.completeScreen(onNavigateBack: () -> Unit) {
  composable(route = COMPLETE_NAVIGATION_ROUTE) {
    CompleteRoute(onNavigateBack)
  }
}
