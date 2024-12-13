package com.nexters.bandalart.feature.home.presenter

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.nexters.bandalart.core.common.extension.bitmapToFileUri
import com.nexters.bandalart.core.common.extension.externalShareForBitmap
import com.nexters.bandalart.core.common.extension.saveImageToGallery
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.core.domain.repository.InAppUpdateRepository
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.home.HomeScreen
import com.nexters.bandalart.feature.home.HomeScreen.Event
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber
import com.nexters.bandalart.feature.complete.CompleteScreen
import com.slack.circuit.retained.produceRetainedState

// TODO Presenter 에 context 못쓰지 않나? 여기서 이벤트 구현하는게 맞나? -> 쓸 수 있음
// TODO Navigation 을 app 모듈 또는 main 모듈에서 전역으로 관리하는게 아니다보니, feature 모듈간에 순환참조가 발생할 것 같은데...
class HomePresenter @AssistedInject constructor(
    private val bandalartRepository: BandalartRepository,
    private val inAppUpdateRepository: InAppUpdateRepository,
    @ApplicationContext private val context: Context,
    @Assisted private val navigator: Navigator,
) : Presenter<HomeScreen.State> {

    @Composable
    override fun present(): HomeScreen.State {
        // TODO rememberCoroutineScope 와 뭔 차이인지 확인
        // val scope = rememberCoroutineScope()
        val scope = rememberStableCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val appVersion = remember {
            try {
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.tag("AppVersion").e(e, "Failed to get package info")
                "Unknown"
            }
        }

        var bandalartCaptureUrl by remember { mutableStateOf("") }

        val isUpdateAlreadyRejected by produceRetainedState(initialValue = false) {
            inAppUpdateRepository.isUpdateAlreadyRejected(updateVersionCode)
        }

        return HomeScreen.State(
            bandalartChartUrl = bandalartCaptureUrl,
        ) { event ->
            when (event) {
                is Event.NavigateToComplete -> navigator.goTo(
                    CompleteScreen(
                        bandalartId = event.id,
                        bandalartTitle = event.title,
                        bandalartProfileEmoji = event.profileEmoji,
                        bandalartChartImageUri = event.bandalartChartImageUri,
                    )
                )

                is Event.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(context),
                            duration = SnackbarDuration.Short,
                        )
                    }
                }

                is Event.ShowToast -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
                }

                is Event.SaveBandalart -> {
                    scope.launch {
                        context.saveImageToGallery(event.bitmap)
                        Toast.makeText(context, context.getString(R.string.save_bandalart_image), Toast.LENGTH_SHORT).show()
                    }
                }

                is Event.ShareBandalart -> {
                    context.externalShareForBitmap(event.bitmap)
                }

                is Event.CaptureBandalart -> {
                    bandalartCaptureUrl = context.bitmapToFileUri(event.bitmap).toString()
                }

                is Event.ShowAppVersion -> {
                    Toast.makeText(context, context.getString(R.string.app_version_info, appVersion), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: HomeScreen,
            navigator: Navigator,
        ): HomePresenter
    }
}
