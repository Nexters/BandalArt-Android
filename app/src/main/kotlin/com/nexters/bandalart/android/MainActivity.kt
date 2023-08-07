package com.nexters.bandalart.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.bandalart.android.core.ui.component.LoadingWheel
import com.nexters.bandalart.android.core.ui.theme.BandalartTheme
import com.nexters.bandalart.android.core.ui.theme.MainColor
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
              LoadingWheel(progressColor = MainColor)
            }
            uiState.error != null -> {
              //TODO ErrorScreen()
            }
            else -> {
              BandalartApp(
                if (uiState.isLoggedIn) HOME_NAVIGATION_ROUTE else ONBOARDING_NAVIGATION_ROUTE
              )
            }
          }
        }
      }
    }
  }
}
