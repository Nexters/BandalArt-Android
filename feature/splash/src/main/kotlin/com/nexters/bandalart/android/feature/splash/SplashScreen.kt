package com.nexters.bandalart.android.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavOptions
import com.nexters.bandalart.android.core.designsystem.theme.Gray50
import com.nexters.bandalart.android.core.ui.ObserveAsEvents
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.component.AppTitle
import com.nexters.bandalart.android.core.ui.component.LoadingScreen
import com.nexters.bandalart.android.core.ui.component.NetworkErrorAlertDialog
import com.nexters.bandalart.android.feature.splash.navigation.SPLASH_NAVIGATION_ROUTE

@Composable
internal fun SplashRoute(
  navigateToOnBoarding: (NavOptions) -> Unit,
  navigateToHome: (NavOptions) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: SplashViewModel = hiltViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  ObserveAsEvents(flow = viewModel.eventFlow) { event ->
    when (event) {
      is SplashUiEvent.NavigateToOnBoarding -> {
        val options = NavOptions.Builder()
          .setPopUpTo(SPLASH_NAVIGATION_ROUTE, inclusive = true)
          .build()
        navigateToOnBoarding(options)
      }

      is SplashUiEvent.NavigateToHome -> {
        val options = NavOptions.Builder()
          .setPopUpTo(SPLASH_NAVIGATION_ROUTE, inclusive = true)
          .build()
        navigateToHome(options)
      }
    }
  }

  SplashScreen(
    uiState = uiState,
    navigateToOnBoarding = viewModel::navigateToOnBoarding,
    navigateToHome = viewModel::navigateToHome,
    openNetworkErrorAlertDialog = viewModel::openNetworkErrorAlertDialog,
    createGuestLoginToken = viewModel::createGuestLoginToken,
    modifier = modifier,
  )
}

@Composable
fun SplashScreen(
  uiState: SplashUiState,
  navigateToOnBoarding: () -> Unit,
  navigateToHome: () -> Unit,
  openNetworkErrorAlertDialog: (Boolean) -> Unit,
  createGuestLoginToken: () -> Unit,
  modifier: Modifier = Modifier,
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

    // TODO 에러처리 보완
    !uiState.isLoggedIn -> {
      navigateToOnBoarding()
    }

    uiState.isLoggedIn -> {
      navigateToHome()
    }
  }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = Gray50,
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      Row(
        modifier = Modifier.align(Alignment.Center),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Image(
          imageVector = ImageVector.vectorResource(
            id = com.nexters.bandalart.android.core.designsystem.R.drawable.ic_app,
          ),
          contentDescription = context.getString(R.string.app_icon_description),
        )
        Spacer(modifier = Modifier.width(10.dp))
        AppTitle()
      }
    }
  }
}
