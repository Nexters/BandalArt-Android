package com.nexters.bandalart.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberBandalartAppState(
  navController: NavHostController = rememberNavController(),
): BandalartAppState {
  return remember(
    key1 = navController,
  ) {
    BandalartAppState(
      navController = navController,
    )
  }
}

@Stable
class BandalartAppState(
  val navController: NavHostController,
)
