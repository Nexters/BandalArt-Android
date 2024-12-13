package com.nexters.bandalart.feature.onboarding.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.nexters.bandalart.core.domain.repository.OnboardingRepository
import com.nexters.bandalart.feature.onboarding.OnBoardingScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
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
        val onboardingCompleted by produceRetainedState(initialValue = false) {
            repository.getOnboardingCompletedStatus()
        }

        return OnBoardingScreen.State { event ->
            when (event) {
                is OnBoardingScreen.Event.NavigateToHome -> navigator.goTo(
                    // TODO 이러면 Onboarding 모듈이 Home 모듈을 알아야 하는데?
                    HomeScreen(event.)
                )
            }
        }
    }

    @CircuitInject(OnBoardingScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: OnBoardingScreen,
            navigator: Navigator,
        ): OnboardingPresenter
    }
}
