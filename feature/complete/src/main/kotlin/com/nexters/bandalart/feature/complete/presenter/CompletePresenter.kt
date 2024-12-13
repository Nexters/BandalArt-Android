package com.nexters.bandalart.feature.complete.presenter

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import com.nexters.bandalart.core.common.extension.saveUriToGallery
import com.nexters.bandalart.core.common.extension.shareImage
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.complete.CompleteScreen
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
    @ApplicationContext private val context: Context,
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
            screen: CompleteScreen,
            navigator: Navigator,
        ): CompletePresenter
    }
}
