package com.nexters.bandalart.feature.onboarding.presenter

import androidx.compose.runtime.Composable
import com.nexters.bandalart.core.domain.repository.OnboardingRepository
import com.nexters.bandalart.feature.home.HomeScreen
import com.nexters.bandalart.feature.onboarding.OnboardingScreen
import com.nexters.bandalart.feature.onboarding.OnboardingScreen.State
import com.nexters.bandalart.feature.onboarding.OnboardingScreen.Event
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import com.slack.circuit.runtime.popUntil
import kotlinx.coroutines.launch

class OnboardingPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val repository: OnboardingRepository,
) : Presenter<State> {

    @Composable
    override fun present(): State {
        val scope = rememberStableCoroutineScope()

        return State { event ->
            when (event) {
                is Event.NavigateToHome -> {
                    scope.launch {
                        repository.setOnboardingCompletedStatus(true)
                        navigator.apply {
                            popUntil { it is OnboardingScreen }
                            goTo(HomeScreen)
                        }
                    }
                }
            }
        }
    }

    @CircuitInject(OnboardingScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): OnboardingPresenter
    }
}
