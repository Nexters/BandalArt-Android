package com.nexters.bandalart.feature.home.ui.bandalart

import androidx.compose.runtime.Composable
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuitx.overlays.BottomSheetOverlay

class BandalartEmojiBottomSheetOverlay(
    val title: String,
): BottomSheetOverlay<BandalartEmojiBottomSheetResult> {
    @Composable
    override fun Content(navigator: OverlayNavigator<BandalartEmojiBottomSheetResult>) {
        navigator.finish(result)
    }
}
