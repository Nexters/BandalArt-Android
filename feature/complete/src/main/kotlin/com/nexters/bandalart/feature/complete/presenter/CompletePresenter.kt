package com.nexters.bandalart.feature.complete.presenter

import androidx.compose.runtime.Composable
import com.nexters.bandalart.feature.complete.CompleteScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class CompletePresenter @AssistedInject constructor(
    @Assisted private val screen: CompleteScreen,
    @Assisted private val navigator: Navigator,
) : Presenter<CompleteScreen.State> {

    @Composable
    override fun present(): CompleteScreen.State {
        return CompleteScreen.State(
            id = 0L,
            title = "",
            profileEmoji = "",
            isShared = false,
            bandalartChartImageUri = "",
        ) { event ->
            when (event) {
                is CompleteScreen.Event.NavigateBack -> navigator.pop()
            }
        }
    }

    @CircuitInject(CompleteScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: CompleteScreen,
            navigator: Navigator,
        ): CompletePresenter
    }
}
