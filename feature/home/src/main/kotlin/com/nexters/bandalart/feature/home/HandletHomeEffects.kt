package com.nexters.bandalart.feature.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.layer.GraphicsLayer
import com.nexters.bandalart.feature.home.HomeScreen.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SNACKBAR_DURATION = 1500L

@Composable
internal fun HandleHomeEffects(
    state: State,
    snackbarHostState: SnackbarHostState,
    homeGraphicsLayer: GraphicsLayer,
    completeGraphicsLayer: GraphicsLayer,
    eventSink: (HomeScreen.Event) -> Unit
) {
    LaunchedEffect(state.sideEffect) {
        when (val sideEffect = state.sideEffect) {
            is HomeScreen.SideEffect.ShowSnackbar -> {
                val job = launch {
                    snackbarHostState.showSnackbar(sideEffect.message)
                }
                delay(SNACKBAR_DURATION)
                job.cancel()
                eventSink(HomeScreen.Event.InitSideEffect)
            }
            null -> {}
        }
    }

    LaunchedEffect(state.isSharing) {
        if (state.isSharing) {
            eventSink(HomeScreen.Event.OnShareRequested(homeGraphicsLayer.toImageBitmap()))
        }
    }

    LaunchedEffect(state.isCapturing) {
        if (state.isCapturing) {
            val bitmap = completeGraphicsLayer.toImageBitmap()
            if (state.isBandalartCompleted) {
                eventSink(HomeScreen.Event.OnCaptureRequested(bitmap))
            } else {
                eventSink(HomeScreen.Event.OnSaveRequested(bitmap))
            }
        }
    }
}
