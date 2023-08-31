@file:OptIn(ExperimentalMaterial3Api::class)

package com.nexters.bandalart.android.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.nexters.bandalart.android.feature.home.HomeViewModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartEmojiModel

@Composable
fun BandalartEmojiBottomSheet(
  bandalartKey: String,
  cellKey: String,
  currentEmoji: String?,
  onResult: (
    bottomSheetState: Boolean,
    bottomSheetDataChangedState: Boolean,
  ) -> Unit,
  viewModel: HomeViewModel = hiltViewModel(),
) {
  val scope = rememberCoroutineScope()
  val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = {
      onResult(false, false)
    },
    modifier = Modifier
      .wrapContentSize(),
    sheetState = bottomSheetState,
    dragHandle = null,
  ) {
    Column(
      content = BandalartEmojiPicker(
        currentEmoji = currentEmoji,
        isBottomSheet = true,
        onResult = { currentEmojiResult, openEmojiBottomSheetResult ->
          viewModel.updateBandalartEmoji(
            bandalartKey,
            cellKey,
            UpdateBandalartEmojiModel(profileEmoji = currentEmojiResult),
          )
          onResult(false, true)
        },
        emojiPickerScope = scope,
        emojiPickerState = bottomSheetState,
      ),
    )
  }
}
