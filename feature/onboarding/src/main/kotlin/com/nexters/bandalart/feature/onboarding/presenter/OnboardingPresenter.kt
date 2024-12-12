package com.nexters.bandalart.feature.onboarding.presenter

import androidx.compose.runtime.Composable
import com.nexters.bandalart.core.domain.repository.OnboardingRepository
import com.nexters.bandalart.feature.onboarding.OnBoardingScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class OnboardingPresenter @AssistedInject constructor(
    private val repository: OnboardingRepository,
    @Assisted private val navigator: Navigator,
) : Presenter<OnBoardingScreen.State> {

    @Composable
    override fun present(): OnBoardingScreen.State {
        return OnBoardingScreen.State { event ->
            when (event) {
                is OnBoardingScreen.Event.NavigateToHome -> navigator.goTo(
                    // HomeScreen()
                )
            }
        }
    }

    @CircuitInject(OnBoardingScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): OnboardingPresenter
    }
}
