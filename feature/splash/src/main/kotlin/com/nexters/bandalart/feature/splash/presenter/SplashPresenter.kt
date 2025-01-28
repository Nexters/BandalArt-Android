package com.nexters.bandalart.feature.splash.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.nexters.bandalart.core.domain.repository.OnboardingRepository
import com.nexters.bandalart.feature.home.HomeScreen
import com.nexters.bandalart.feature.onboarding.OnboardingScreen
import com.nexters.bandalart.feature.splash.SplashScreen
import com.nexters.bandalart.feature.splash.SplashScreen.Event
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope


@CircuitInject(SplashScreen::class, AppScope::class)
@Inject
class SplashPresenter(
    @Assisted private val navigator: Navigator,
    private val repository: OnboardingRepository,
) : Presenter<SplashScreen.State> {
    @Composable
    override fun present(): SplashScreen.State {
        val scope = rememberCoroutineScope()
        val isOnboardingCompleted by repository.flowIsOnboardingCompleted().collectAsRetainedState(false)

        return SplashScreen.State(
            isOnboardingCompleted = isOnboardingCompleted,
        ) { event ->
            when (event) {
                is Event.CheckOnboardingStatus -> {
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
