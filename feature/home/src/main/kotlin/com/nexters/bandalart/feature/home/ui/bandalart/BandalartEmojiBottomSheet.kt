package com.nexters.bandalart.feature.home.ui.bandalart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.feature.home.viewmodel.BottomSheetUiAction
import com.nexters.bandalart.feature.home.model.UpdateBandalartEmojiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandalartEmojiBottomSheet(
    bandalartId: Long,
    cellId: Long,
    currentEmoji: String?,
    updateBandalartEmoji: (Long, Long, UpdateBandalartEmojiModel) -> Unit,
    onResult: (
        bottomSheetState: Boolean,
        bottomSheetDataChangedState: Boolean,
    ) -> Unit,
    onBottomSheetUiAction: (BottomSheetUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = {
            onResult(false, false)
        },
        modifier = modifier.wrapContentSize(),
        sheetState = bottomSheetState,
        dragHandle = null,
    ) {
        Column {
            BandalartEmojiPicker(
                currentEmoji = currentEmoji,
                isBottomSheet = true,
                onResult = { currentEmojiResult, openEmojiBottomSheetResult ->
                    updateBandalartEmoji(
                        bandalartId,
                        cellId,
                        UpdateBandalartEmojiModel(profileEmoji = currentEmojiResult),
                    )
                    onResult(false, true)
                },
                emojiPickerState = bottomSheetState,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun BandalartEmojiBottomSheetPreview() {
    BandalartTheme {
        BandalartEmojiBottomSheet(
            bandalartId = 0L,
            cellId = 0L,
            currentEmoji = "😎",
            updateBandalartEmoji = { _, _, _ -> },
            onResult = { _, _ -> },
            onBottomSheetUiAction = {},
        )
    }
}
