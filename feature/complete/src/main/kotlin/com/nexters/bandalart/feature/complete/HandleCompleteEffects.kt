package com.nexters.bandalart.feature.complete

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.nexters.bandalart.core.common.extension.saveUriToGallery
import com.nexters.bandalart.core.common.extension.shareImage
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.complete.CompleteScreen.Event
import com.nexters.bandalart.feature.complete.CompleteScreen.SideEffect
import com.nexters.bandalart.feature.complete.CompleteScreen.State

@Composable
internal fun HandleCompleteEffects(
    state: State,
    eventSink: (Event) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(state.sideEffect) {
        when (val sideEffect = state.sideEffect) {
            is SideEffect.ShowToast -> {
                Toast.makeText(context, sideEffect.message.asString(context), Toast.LENGTH_SHORT).show()
            }

            is SideEffect.SaveImage -> {
                context.saveUriToGallery(sideEffect.imageUri)
                Toast.makeText(context, context.getString(R.string.save_bandalart_image), Toast.LENGTH_SHORT).show()
            }

            is SideEffect.ShareImage -> {
                context.shareImage(sideEffect.imageUri)
            }

            null -> {}
        }

        if (state.sideEffect != null) {
            eventSink(Event.InitSideEffect)
        }
    }
}
