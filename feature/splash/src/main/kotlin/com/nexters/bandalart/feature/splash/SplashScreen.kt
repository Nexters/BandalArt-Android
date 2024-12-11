package com.nexters.bandalart.feature.splash

import android.app.Activity
import android.window.SplashScreen
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavOptions
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.nexters.bandalart.core.common.extension.await
import com.nexters.bandalart.core.common.extension.findActivity
import com.nexters.bandalart.core.common.utils.ObserveAsEvents
import com.nexters.bandalart.core.common.utils.isValidImmediateAppUpdate
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray50
import com.nexters.bandalart.core.navigation.Route
import com.nexters.bandalart.core.ui.DevicePreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.component.AppTitle
import timber.log.Timber
import com.nexters.bandalart.core.designsystem.R as DesignR

// TODO AddOnCompleteListener 와 AddOnSuccessListener 는 다르다
@Suppress("TooGenericExceptionCaught")
@Composable
internal fun SplashRoute(
    navigateToOnBoarding: (NavOptions) -> Unit,
    navigateToHome: (NavOptions) -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val appUpdateManager: AppUpdateManager = remember { AppUpdateManagerFactory.create(context) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val lifecycleState by lifecycle.currentStateFlow.collectAsStateWithLifecycle()

    val appUpdateResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_CANCELED) {
            activity.finish()
        }
    }

    LaunchedEffect(Unit) {
        try {
            val appUpdateInfo = appUpdateManager.appUpdateInfo.await()

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                val availableVersionCode = appUpdateInfo.availableVersionCode()

                if (isValidImmediateAppUpdate(availableVersionCode) &&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        appUpdateResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                    )
                } else {
                    viewModel.checkOnboardingStatus()
                }
            } else {
                viewModel.checkOnboardingStatus()
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to check for immediate update")
            viewModel.checkOnboardingStatus()
        }
    }

    // LifecycleResumeEffect 는 내부에 suspend 함수를 사용할 수 없다.
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED) {
            try {
                val appUpdateInfo = appUpdateManager.appUpdateInfo.await()
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        appUpdateResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to check update status on resume")
            }
        }
    }

    ObserveAsEvents(flow = viewModel.uiEvent) { event ->
        when (event) {
            is SplashUiEvent.NavigateToOnBoarding -> {
                val options = NavOptions.Builder()
                    .setPopUpTo(Route.Splash, inclusive = true)
                    .build()
                navigateToOnBoarding(options)
            }

            is SplashUiEvent.NavigateToHome -> {
                val options = NavOptions.Builder()
                    .setPopUpTo(Route.Splash, inclusive = true)
                    .build()
                navigateToHome(options)
            }
        }
    }

    SplashScreen()
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
