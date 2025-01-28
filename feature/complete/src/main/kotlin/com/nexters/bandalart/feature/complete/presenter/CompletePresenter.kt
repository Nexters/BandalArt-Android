package com.nexters.bandalart.feature.complete.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nexters.bandalart.core.common.utils.UiText
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.complete.CompleteScreen
import com.nexters.bandalart.feature.complete.CompleteScreen.Event
import com.nexters.bandalart.feature.complete.CompleteScreen.State
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(CompleteScreen::class, AppScope::class)
@Inject
class CompletePresenter(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: CompleteScreen,
    private val bandalartRepository: BandalartRepository,
) : Presenter<State> {

    @Composable
    override fun present(): State {
        var sideEffect by rememberRetained { mutableStateOf<CompleteScreen.SideEffect?>(null) }

        LaunchedEffect(Unit) {
            bandalartRepository.upsertBandalartId(
                bandalartId = screen.bandalartId,
                isCompleted = true,
            )
        }

        fun showToast(message: UiText) {
            sideEffect = CompleteScreen.SideEffect.ShowToast(message)
        }

        fun clearSideEffect() {
            sideEffect = null
        }

        return State(
            id = 0L,
            title = "",
            profileEmoji = "",
            isShared = false,
            bandalartChartImageUri = "",
        ) { event ->
            when (event) {
                is Event.NavigateBack -> navigator.pop()
                is Event.SaveBandalart -> {
                    sideEffect = CompleteScreen.SideEffect.SaveImage(event.imageUri)
                    showToast(UiText.StringResource(R.string.save_bandalart_image))
                }

                is Event.ShareBandalart -> {
                    sideEffect = CompleteScreen.SideEffect.ShareImage(event.imageUri)
                }

                is Event.InitSideEffect -> clearSideEffect()
            }
        }
    }
}
