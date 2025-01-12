package com.nexters.bandalart.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray50
import com.nexters.bandalart.core.ui.DevicePreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.component.AppTitle
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize
import com.nexters.bandalart.core.designsystem.R as DesignR

@Parcelize
data object SplashScreen : Screen {
    data class State(
        val isLoading: Boolean,
        val isOnboardingCompleted: Boolean,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object CheckOnboardingStatus : Event
    }
}

// TODO 인앱 업데이트 복원
@CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Splash(
    state: SplashScreen.State,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
//    val context = LocalContext.current
//    val activity = context.findActivity()
//    val appUpdateManager: AppUpdateManager = remember { AppUpdateManagerFactory.create(context) }
//    val lifecycle = LocalLifecycleOwner.current.lifecycle
//    val lifecycleState by lifecycle.currentStateFlow.collectAsStateWithLifecycle()
//
//    val appUpdateResultLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartIntentSenderForResult(),
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_CANCELED) {
//            activity.finish()
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        try {
//            val appUpdateInfo = appUpdateManager.appUpdateInfo.await()
//
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
//                val availableVersionCode = appUpdateInfo.availableVersionCode()
//
//                if (isValidImmediateAppUpdate(availableVersionCode) &&
//                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
//                ) {
//                    appUpdateManager.startUpdateFlowForResult(
//                        appUpdateInfo,
//                        appUpdateResultLauncher,
//                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
//                    )
//                } else {
//                    eventSink(SplashScreen.Event.CheckOnboardingStatus)
//                }
//            } else {
//                eventSink(SplashScreen.Event.CheckOnboardingStatus)
//            }
//        } catch (e: Exception) {
//            Timber.e(e, "Failed to check for immediate update")
//            eventSink(SplashScreen.Event.CheckOnboardingStatus)
//        }
//    }
//
//    // LifecycleResumeEffect 는 내부에 suspend 함수를 사용할 수 없다.
//    LaunchedEffect(lifecycleState) {
//        if (lifecycleState == Lifecycle.State.RESUMED) {
//            try {
//                val appUpdateInfo = appUpdateManager.appUpdateInfo.await()
//                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
//                    appUpdateManager.startUpdateFlowForResult(
//                        appUpdateInfo,
//                        appUpdateResultLauncher,
//                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
//                    )
//                }
//            } catch (e: Exception) {
//                Timber.e(e, "Failed to check update status on resume")
//            }
//        }
//    }

    LaunchedEffect(state.isLoading, state.isOnboardingCompleted) {
        if (!state.isLoading) {
            eventSink(SplashScreen.Event.CheckOnboardingStatus)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Gray50),
    ) {
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

@DevicePreview
@Composable
private fun SplashPreview() {
    BandalartTheme {
        Splash(
            state = SplashScreen.State(
                isLoading = false,
                isOnboardingCompleted = false,
                eventSink = {},
            ),
        )
    }
}
