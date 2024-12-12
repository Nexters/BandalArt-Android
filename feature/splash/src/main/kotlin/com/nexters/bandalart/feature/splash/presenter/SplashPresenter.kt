package com.nexters.bandalart.feature.splash.presenter

import androidx.compose.runtime.Composable
import com.nexters.bandalart.feature.splash.SplashScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class SplashPresenter @AssistedInject constructor(
    @Assisted private val screen: SplashScreen,
    @Assisted private val navigator: Navigator,
) : Presenter<SplashScreen.State> {

    @Composable
    override fun present(): SplashScreen.State {
        return SplashScreen.State { event ->
            when (event) {
                is SplashScreen.Event.NavigateToOnBoarding -> navigator.goTo(
                    // OnBoardingScreen()
                )
                is SplashScreen.Event.NavigateToHome -> navigator.goTo(
                    // HomeScreen()
                )
            }
        }
    }

    @CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: SplashScreen,
            navigator: Navigator,
        ): SplashPresenter
    }
}
