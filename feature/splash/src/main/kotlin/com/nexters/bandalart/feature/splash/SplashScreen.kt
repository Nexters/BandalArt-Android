package com.nexters.bandalart.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavOptions
import com.nexters.bandalart.core.ui.DevicePreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.component.AppTitle
import com.nexters.bandalart.core.common.utils.ObserveAsEvents
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray50
import com.nexters.bandalart.feature.splash.navigation.SPLASH_NAVIGATION_ROUTE
import com.nexters.bandalart.core.designsystem.R as DesignR

@Composable
internal fun SplashRoute(
    navigateToOnBoarding: (NavOptions) -> Unit,
    navigateToHome: (NavOptions) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    ObserveAsEvents(flow = viewModel.uiEvent) { event ->
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
        modifier = modifier,
    )
}

@Composable
internal fun SplashScreen(
    modifier: Modifier = Modifier,
) {
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
                    imageVector = ImageVector.vectorResource(DesignR.drawable.ic_app),
                    contentDescription = stringResource(R.string.app_icon_description),
                )
                Spacer(modifier = Modifier.width(10.dp))
                AppTitle()
            }
        }
    }
}

@DevicePreview
@Composable
private fun SplashScreenPreview() {
    BandalartTheme {
        SplashScreen()
    }
}
