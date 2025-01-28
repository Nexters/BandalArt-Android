package com.nexters.bandalart.feature.onboarding.presenter

import androidx.compose.runtime.Composable
import com.nexters.bandalart.core.domain.repository.OnboardingRepository
import com.nexters.bandalart.feature.home.HomeScreen
import com.nexters.bandalart.feature.onboarding.OnboardingScreen
import com.nexters.bandalart.feature.onboarding.OnboardingScreen.Event
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(OnboardingScreen::class, AppScope::class)
@Inject
class OnboardingPresenter(
    @Assisted private val navigator: Navigator,
    private val repository: OnboardingRepository,
) : Presenter<OnboardingScreen.State> {
    @Composable
    override fun present(): OnboardingScreen.State {
        val scope = rememberStableCoroutineScope()
        return OnboardingScreen.State { event ->
            when (event) {
                is Event.NavigateToHome -> {
                    scope.launch {
                        repository.setOnboardingCompletedStatus(true)
                        navigator.resetRoot(HomeScreen)
                    }
                }
            }
        }
    }
}
