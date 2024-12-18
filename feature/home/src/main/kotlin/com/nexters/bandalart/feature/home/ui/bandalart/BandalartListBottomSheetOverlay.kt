package com.nexters.bandalart.feature.home.ui.bandalart

import androidx.compose.runtime.Composable
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuitx.overlays.BottomSheetOverlay

class BandalartListBottomSheetOverlay(
    val title: String,
): BottomSheetOverlay<BandalartListBottomSheetResult> {
    @Composable
    override fun Content(navigator: OverlayNavigator<BandalartListBottomSheetResult>) {
        navigator.finish(result)
    }
}
