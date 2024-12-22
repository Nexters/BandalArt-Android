package com.nexters.bandalart.feature.home.ui.bandalart

sealed interface BottomSheetResult {
    data object Add : BottomSheetResult
    data object Dismiss : BottomSheetResult
}

//@OptIn(ExperimentalMaterial3Api::class)
//class BandalartEmojiBottomSheetOverlay(
//    val currentEmoji: String,
//): BottomSheetOverlay<BottomSheetResult> {
//    @Composable
//    override fun Content(navigator: OverlayNavigator<BottomSheetResult>) {
//        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
//
//        ModalBottomSheet(
//            onDismissRequest = {
//                navigator.finish(BottomSheetResult.Dismiss)
//            },
//            modifier = Modifier.wrapContentSize(),
//            sheetState = bottomSheetState,
//            dragHandle = null,
//        ) {
//            Column {
//                BandalartEmojiPicker(
//                    currentEmoji = currentEmoji,
//                    isBottomSheet = true,
//                    onEmojiSelect = { selectedEmoji ->
//                        eventSink(
//                            Event.OnEmojiSelected(
//                                bandalartId,
//                                cellId,
//                                UpdateBandalartEmojiEntity(profileEmoji = selectedEmoji),
//                            ),
//                        )
//                    },
//                )
//            }
//        }
//    }
//}
