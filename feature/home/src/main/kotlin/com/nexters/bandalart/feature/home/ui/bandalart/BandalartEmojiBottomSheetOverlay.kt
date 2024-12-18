package com.nexters.bandalart.feature.home.ui.bandalart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nexters.bandalart.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.feature.home.HomeScreen.Event
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuitx.overlays.BottomSheetOverlay

sealed interface BottomSheetResult {
    data object Add : BottomSheetResult
    data object Dismiss : BottomSheetResult
}

@OptIn(ExperimentalMaterial3Api::class)
class BandalartEmojiBottomSheetOverlay(
    val currentEmoji: String,
): BottomSheetOverlay<BottomSheetResult> {
    @Composable
    override fun Content(navigator: OverlayNavigator<BottomSheetResult>) {
        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = {
                navigator.finish(BottomSheetResult.Dismiss)
            },
            modifier = Modifier.wrapContentSize(),
            sheetState = bottomSheetState,
            dragHandle = null,
        ) {
            Column {
                BandalartEmojiPicker(
                    currentEmoji = currentEmoji,
                    isBottomSheet = true,
                    onEmojiSelect = { selectedEmoji ->
                        eventSink(
                            Event.OnEmojiSelected(
                                bandalartId,
                                cellId,
                                UpdateBandalartEmojiEntity(profileEmoji = selectedEmoji),
                            ),
                        )
                    },
                )
            }
        }
    }
}
