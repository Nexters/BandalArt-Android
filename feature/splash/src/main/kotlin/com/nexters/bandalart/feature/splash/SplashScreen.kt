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
        val eventSink: (Event) -> Unit,
    ): CircuitUiState
    sealed interface Event : CircuitUiEvent {
        data object NavigateToOnBoarding : Event
        data object NavigateToHome : Event
    }
}

//@Suppress("TooGenericExceptionCaught")
//@Composable
//internal fun SplashRoute(
//    navigateToOnBoarding: (NavOptions) -> Unit,
//    navigateToHome: (NavOptions) -> Unit,
//    viewModel: SplashViewModel = hiltViewModel(),
//) {
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
//                    viewModel.checkOnboardingStatus()
//                }
//            } else {
//                viewModel.checkOnboardingStatus()
//            }
//        } catch (e: Exception) {
//            Timber.e(e, "Failed to check for immediate update")
//            viewModel.checkOnboardingStatus()
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
//
//    ObserveAsEvents(flow = viewModel.uiEvent) { event ->
//        when (event) {
//            is SplashUiEvent.NavigateToOnBoarding -> {
//                val options = NavOptions.Builder()
//                    .setPopUpTo(Route.Splash, inclusive = true)
//                    .build()
//                navigateToOnBoarding(options)
//            }
//
//            is SplashUiEvent.NavigateToHome -> {
//                val options = NavOptions.Builder()
//                    .setPopUpTo(Route.Splash, inclusive = true)
//                    .build()
//                navigateToHome(options)
//            }
//        }
//    }
//
//    SplashScreen()
//}

@CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Splash(
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
        Splash()
    }
}
