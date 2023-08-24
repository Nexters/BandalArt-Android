package com.nexters.bandalart.android.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavOptions
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.theme.Gray50

@Composable
internal fun SplashRoute(
  navigateToOnBoarding: (NavOptions) -> Unit,
  navigateToHome: (NavOptions) -> Unit,
) {
  SplashScreen(
    navigateToOnBoarding = { options ->
      navigateToOnBoarding(options)
    },
    navigateToHome = { options ->
      navigateToHome(options)
    },
  )
}

@Composable
fun SplashScreen(
  modifier: Modifier = Modifier,
  navigateToOnBoarding: (NavOptions) -> Unit,
  navigateToHome: (NavOptions) -> Unit,
) {
  val context = LocalContext.current
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