package com.nexters.bandalart.feature.complete.presenter

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.nexters.bandalart.core.common.extension.saveUriToGallery
import com.nexters.bandalart.core.common.extension.shareImage
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.complete.CompleteScreen
import com.nexters.bandalart.feature.complete.CompleteScreen.State
import com.nexters.bandalart.feature.complete.CompleteScreen.Event
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

class CompletePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: CompleteScreen,
    @ApplicationContext private val context: Context,
    private val bandalartRepository: BandalartRepository,
) : Presenter<State> {

    @Composable
    override fun present(): State {
        LaunchedEffect(Unit) {
            bandalartRepository.upsertBandalartId(
                bandalartId = screen.bandalartId,
                isCompleted = true,
            )
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
                    context.saveUriToGallery(event.imageUri)
                    Toast.makeText(context, context.getString(R.string.save_bandalart_image), Toast.LENGTH_SHORT).show()
                }

                is Event.ShareBandalart -> {
                    context.shareImage(event.imageUri)
                }
            }
        }
    }

    @CircuitInject(CompleteScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            navigator: Navigator,
            screen: CompleteScreen,
        ): CompletePresenter
    }
}
