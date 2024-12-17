package com.nexters.bandalart.feature.splash.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.nexters.bandalart.core.domain.repository.OnboardingRepository
import com.nexters.bandalart.feature.home.HomeScreen
import com.nexters.bandalart.feature.onboarding.OnboardingScreen
import com.nexters.bandalart.feature.splash.SplashScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class SplashPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val repository: OnboardingRepository,
) : Presenter<SplashScreen.State> {

    @Composable
    override fun present(): SplashScreen.State {
        val isOnboardingCompleted by produceRetainedState(initialValue = false) {
            repository.getOnboardingCompletedStatus()
        }

        return SplashScreen.State(
            isOnboardingCompleted = isOnboardingCompleted,
        ) { event ->
            when (event) {
                is SplashScreen.Event.CheckOnboardingStatus -> {
                    if (isOnboardingCompleted) {
                        navigator.apply {
                            pop()
                            goTo(HomeScreen)
                        }
                    } else {
                        navigator.apply {
                            pop()
                            goTo(OnboardingScreen)
                        }
                    }
                }
            }
        }
    }

    @CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SplashPresenter
    }
}
