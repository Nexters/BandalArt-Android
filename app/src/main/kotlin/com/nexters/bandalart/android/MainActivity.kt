package com.nexters.bandalart.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.bandalart.android.core.ui.component.LoadingScreen
import com.nexters.bandalart.android.core.ui.component.NetworkErrorAlertDialog
import com.nexters.bandalart.android.core.ui.theme.BandalartTheme
import com.nexters.bandalart.android.feature.home.navigation.HOME_NAVIGATION_ROUTE
import com.nexters.bandalart.android.feature.onboarding.navigation.ONBOARDING_NAVIGATION_ROUTE
import com.nexters.bandalart.android.ui.BandalartApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()
    setContent {
      val uiState by viewModel.uiState.collectAsStateWithLifecycle()
      BandalartTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background,
        ) {
          when {
            uiState.isLoading -> {
              LoadingScreen(
                modifier = Modifier
                  .fillMaxWidth()
                  .aspectRatio(1f),
              )
            }
            uiState.isNetworkErrorAlertDialogOpened -> {
              NetworkErrorAlertDialog(
                title = "네트워크 문제로 표를\n불러오지 못했어요",
                message = "다시 시도해주시기 바랍니다.",
                onConfirmClick = {
                  viewModel.openNetworkErrorAlertDialog(false)
                  viewModel.createGuestLoginToken()
                },
              )
            }
            !uiState.isLoggedIn -> {
              BandalartApp(ONBOARDING_NAVIGATION_ROUTE)
            }
            uiState.isLoggedIn -> {
              BandalartApp(HOME_NAVIGATION_ROUTE)
            }
          }
        }
      }
    }
  }
}
