package com.nexters.bandalart.android.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavOptions
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.component.LoadingScreen
import com.nexters.bandalart.android.core.ui.component.NetworkErrorAlertDialog
import com.nexters.bandalart.android.core.designsystem.theme.Gray50
import com.nexters.bandalart.android.feature.splash.navigation.SPLASH_NAVIGATION_ROUTE

@Composable
internal fun SplashRoute(
  navigateToOnBoarding: (NavOptions) -> Unit,
  navigateToHome: (NavOptions) -> Unit,
  viewModel: SplashViewModel = hiltViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  SplashScreen(
    uiState = uiState,
    navigateToOnBoarding = navigateToOnBoarding,
    navigateToHome = navigateToHome,
    openNetworkErrorAlertDialog = viewModel::openNetworkErrorAlertDialog,
    createGuestLoginToken = viewModel::createGuestLoginToken,
  )
}

@Composable
fun SplashScreen(
  modifier: Modifier = Modifier,
  uiState: SplashUiState,
  navigateToOnBoarding: (NavOptions) -> Unit,
  navigateToHome: (NavOptions) -> Unit,
  openNetworkErrorAlertDialog: (Boolean) -> Unit,
  createGuestLoginToken: () -> Unit,
) {
  val context = LocalContext.current
  when {
    uiState.isLoading -> {
      LoadingScreen(modifier = Modifier.fillMaxSize())
    }
    uiState.isNetworkErrorAlertDialogOpened -> {
      NetworkErrorAlertDialog(
        title = stringResource(R.string.network_error_dialog_title),
        message = stringResource(R.string.network_error_dialog_message),
        onConfirmClick = {
          openNetworkErrorAlertDialog(false)
          createGuestLoginToken()
        },
      )
    }
    !uiState.isLoggedIn -> {
      val options = NavOptions.Builder()
        .setPopUpTo(SPLASH_NAVIGATION_ROUTE, inclusive = true)
        .build()
      navigateToOnBoarding(options)
    }
    uiState.isLoggedIn -> {
      val options = NavOptions.Builder()
        .setPopUpTo(SPLASH_NAVIGATION_ROUTE, inclusive = true)
        .build()
      navigateToHome(options)
    }
  }

  Surface(
    modifier = modifier
      .fillMaxSize()
      .background(Gray50),
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      val image = painterResource(
        id = com.nexters.bandalart.android.core.designsystem.R.drawable.ic_splash,
      )
      Image(
        painter = image,
        contentDescription = context.getString(R.string.splash_description),
        modifier = Modifier.align(Alignment.Center),
      )
    }
  }
}
