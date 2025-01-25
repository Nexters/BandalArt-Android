package com.nexters.bandalart.feature.home

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.platform.LocalContext
import com.nexters.bandalart.core.common.extension.bitmapToFileUri
import com.nexters.bandalart.core.common.extension.saveImageToGallery
import com.nexters.bandalart.core.common.extension.shareImage
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.home.HomeScreen.Event
import com.nexters.bandalart.feature.home.HomeScreen.SideEffect
import com.nexters.bandalart.feature.home.HomeScreen.State
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SNACKBAR_DURATION = 1500L

@Composable
internal fun HandleHomeEffects(
    state: State,
    snackbarHostState: SnackbarHostState,
    homeGraphicsLayer: GraphicsLayer,
    completeGraphicsLayer: GraphicsLayer,
    eventSink: (Event) -> Unit,
) {
    val context = LocalContext.current
    val appVersion = remember {
        try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Napier.e(e, tag = "AppVersion") { "Failed to get package info" }
            "Unknown"
        }
    }

    LaunchedEffect(state.sideEffect) {
        when (val sideEffect = state.sideEffect) {
            is SideEffect.ShowSnackbar -> {
                val job = launch {
                    snackbarHostState.showSnackbar(sideEffect.message.asString(context))
                }
                delay(SNACKBAR_DURATION)
                job.cancel()
            }

            is SideEffect.ShowToast -> {
                Toast.makeText(context, sideEffect.message.asString(context), Toast.LENGTH_SHORT).show()
            }

            is SideEffect.ShowAppVersion -> {
                Toast.makeText(context, context.getString(R.string.app_version_info, appVersion), Toast.LENGTH_SHORT).show()
            }

            is SideEffect.SaveImage -> {
                context.saveImageToGallery(sideEffect.bitmap)
                Toast.makeText(context, context.getString(R.string.save_bandalart_image), Toast.LENGTH_SHORT).show()
            }

            is SideEffect.ShareImage -> {
                context.bitmapToFileUri(sideEffect.bitmap)?.let { uri ->
                    context.shareImage(uri)
                }
            }

            is SideEffect.CaptureImage -> {
                context.bitmapToFileUri(sideEffect.bitmap)?.let { uri ->
                    eventSink(Event.CaptureFinished(uri.toString()))
                }
            }

            null -> {}
        }

        if (state.sideEffect != null) {
            eventSink(Event.InitSideEffect)
        }
    }

    LaunchedEffect(state.isSharing) {
        if (state.isSharing) {
            eventSink(Event.OnShareRequested(homeGraphicsLayer.toImageBitmap()))
        }
    }

    LaunchedEffect(state.isCapturing) {
        if (state.isCapturing) {
            val bitmap = completeGraphicsLayer.toImageBitmap()
            if (state.isBandalartCompleted) {
                eventSink(Event.OnCaptureRequested(bitmap))
            } else {
                eventSink(Event.OnSaveRequested(bitmap))
            }
        }
    }
}
