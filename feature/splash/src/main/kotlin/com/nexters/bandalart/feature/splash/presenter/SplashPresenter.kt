package com.nexters.bandalart.feature.splash.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.nexters.bandalart.core.domain.repository.OnboardingRepository
import com.nexters.bandalart.feature.home.HomeScreen
import com.nexters.bandalart.feature.onboarding.OnboardingScreen
import com.nexters.bandalart.feature.splash.SplashScreen
import com.nexters.bandalart.feature.splash.SplashScreen.Event
import com.nexters.bandalart.feature.splash.SplashScreen.State
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class SplashPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val repository: OnboardingRepository,
) : Presenter<State> {

    @Composable
    override fun present(): State {
        val scope = rememberCoroutineScope()
        var isLoading by remember { mutableStateOf(true) }
        val isOnboardingCompleted by produceRetainedState(initialValue = false) {
            value = repository.getOnboardingCompletedStatus()
            isLoading = false
        }

        return State(
            isLoading = isLoading,
            isOnboardingCompleted = isOnboardingCompleted,
        ) { event ->
            when (event) {
                is Event.CheckOnboardingStatus -> {
                    if (!isLoading) {
                        scope.launch {
                            if (isOnboardingCompleted) {
                                navigator.resetRoot(HomeScreen)
                            } else {
                                navigator.resetRoot(OnboardingScreen)
                            }
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
