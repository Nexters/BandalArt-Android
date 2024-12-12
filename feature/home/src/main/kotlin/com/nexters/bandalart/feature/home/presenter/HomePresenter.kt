package com.nexters.bandalart.feature.home.presenter

import androidx.compose.runtime.Composable
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.core.domain.repository.InAppUpdateRepository
import com.nexters.bandalart.feature.home.HomeScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

// TODO Presenter 에 context 못쓰지 않나? 여기서 이벤트 구현하는게 맞나?
class HomePresenter @AssistedInject constructor(
    private val bandalartRepository: BandalartRepository,
    private val inAppUpdateRepository: InAppUpdateRepository,
    @Assisted private val navigator: Navigator,
) : Presenter<HomeScreen.State> {

    @Composable
    override fun present(): HomeScreen.State {
        return HomeScreen.State { event ->
            when (event) {
                is HomeScreen.Event.NavigateToComplete -> navigator.goTo(
                    // CompleteScreen()
                )
                is HomeScreen.Event.ShowSnackbar -> {}
                is HomeScreen.Event.ShowToast -> {}
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
