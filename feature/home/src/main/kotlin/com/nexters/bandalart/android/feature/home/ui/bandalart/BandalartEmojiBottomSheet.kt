@file:OptIn(ExperimentalMaterial3Api::class)

package com.nexters.bandalart.android.feature.home.ui.bandalart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nexters.bandalart.android.core.ui.ComponentPreview
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartEmojiModel

@Composable
fun BandalartEmojiBottomSheet(
  bandalartKey: String,
  cellKey: String,
  currentEmoji: String?,
  updateBandalartEmoji: (String, String, UpdateBandalartEmojiModel) -> Unit,
  onResult: (
    bottomSheetState: Boolean,
    bottomSheetDataChangedState: Boolean,
  ) -> Unit,
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
            bandalartKey,
            cellKey,
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
fun BandalartEmojiBottomSheetPreview() {
  BandalartEmojiBottomSheet(
    bandalartKey = "",
    cellKey = "",
    currentEmoji = "😎",
    updateBandalartEmoji = { _, _, _ -> },
    onResult = { _, _ -> },
  )
}
