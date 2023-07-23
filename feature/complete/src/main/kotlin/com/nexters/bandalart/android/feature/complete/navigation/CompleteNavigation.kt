package com.nexters.bandalart.android.feature.complete.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.nexters.bandalart.android.feature.complete.CompleteRoute

const val completeNavigationRoute = "complete_route"

fun NavController.navigateToComplete(navOptions: NavOptions? = null) {
  this.navigate(completeNavigationRoute, navOptions)
}

fun NavGraphBuilder.completeScreen(onNavigateBack: () -> Unit) {
  composable(route = completeNavigationRoute) {
    CompleteRoute(onNavigateBack)
  }
}
