package com.nexters.bandalart.feature.home

import androidx.compose.runtime.Composable
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuitx.overlays.BottomSheetOverlay

class BandalartBottomSheetOverlay(
    val title: String,
): BottomSheetOverlay<BandalartBottomSheetResult> {
    @Composable
    override fun Content(navigator: OverlayNavigator<BandalartBottomSheetResult>) {
        navigator.finish(result)
    }
}
