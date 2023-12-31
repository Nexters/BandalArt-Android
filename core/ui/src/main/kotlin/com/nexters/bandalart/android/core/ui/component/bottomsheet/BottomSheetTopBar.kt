@file:OptIn(ExperimentalMaterial3Api::class)

package com.nexters.bandalart.android.core.ui.component.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.ui.ComponentPreview
import com.nexters.bandalart.android.core.ui.R
import kotlinx.coroutines.launch

@Composable
fun BottomSheetTopBar(
  isMainCell: Boolean,
  isSubCell: Boolean,
  isBlankCell: Boolean,
  bottomSheetState: SheetState,
  onResult: (Boolean, Boolean) -> Unit,
  bottomSheetClosed: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()

  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
  ) {
    BottomSheetTitleText(isMainCell = isMainCell, isSubCell = isSubCell, isBlankCell = isBlankCell)
    IconButton(
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .height(21.dp)
        .aspectRatio(1f),
      onClick = {
        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
          if (!bottomSheetState.isVisible) {
            bottomSheetClosed()
            onResult(false, false)
          }
        }
      },
    ) {
      Icon(
        imageVector = Icons.Default.Clear,
        contentDescription = stringResource(R.string.clear_descrption),
        tint = Gray900,
      )
    }
  }
}

@ComponentPreview
@Composable
fun BottomSheetMainCellTopBarPreview() {
  BottomSheetTopBar(
    isMainCell = true,
    isSubCell = false,
    isBlankCell = false,
    bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onResult = { _, _ -> },
    bottomSheetClosed = {},
  )
}

@ComponentPreview
@Composable
fun BottomSheetSubCellTopBarPreview() {
  BottomSheetTopBar(
    isMainCell = false,
    isSubCell = true,
    isBlankCell = false,
    bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onResult = { _, _ -> },
    bottomSheetClosed = {},
  )
}

@ComponentPreview
@Composable
fun BottomSheetBlankCellTopBarPreview() {
  BottomSheetTopBar(
    isMainCell = false,
    isSubCell = false,
    isBlankCell = true,
    bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onResult = { _, _ -> },
    bottomSheetClosed = {},
  )
}
