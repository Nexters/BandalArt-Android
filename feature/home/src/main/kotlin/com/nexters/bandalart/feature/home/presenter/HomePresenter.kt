package com.nexters.bandalart.feature.home.presenter

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.core.domain.repository.InAppUpdateRepository
import com.nexters.bandalart.feature.home.HomeScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SnackbarDuration = 1500L

// TODO Presenter 에 context 못쓰지 않나? 여기서 이벤트 구현하는게 맞나?
class HomePresenter @AssistedInject constructor(
    private val bandalartRepository: BandalartRepository,
    private val inAppUpdateRepository: InAppUpdateRepository,
    @ApplicationContext private val context: Context,
    @Assisted private val navigator: Navigator,
) : Presenter<HomeScreen.State> {

    @Composable
    override fun present(): HomeScreen.State {
        // rememberCoroutineScope 와 뭔 차이인지 확인
        // val scope = rememberCoroutineScope()
        val scope = rememberStableCoroutineScope()

        return HomeScreen.State { event ->
            when (event) {
                is HomeScreen.Event.NavigateToComplete -> navigator.goTo(
                    // CompleteScreen()
                )
                is HomeScreen.Event.ShowSnackbar -> {
                    scope.launch {
                        val job = launch {
                            // onShowSnackbar(event.message.asString(context))
                        }
                        delay(SnackbarDuration)
                        job.cancel()
                    }
                }
                is HomeScreen.Event.ShowToast -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
                }
                is HomeScreen.Event.SaveBandalart -> {}
                is HomeScreen.Event.ShareBandalart -> {}
                is HomeScreen.Event.CaptureBandalart -> {}
                is HomeScreen.Event.ShowAppVersion -> {}
            }
        }
    }

    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }
}
