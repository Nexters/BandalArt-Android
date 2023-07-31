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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.core.ui.theme.Gray900
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BottomSheetTopBar(
  isMainCell: Boolean,
  isSubCell: Boolean,
  scope: CoroutineScope,
  bottomSheetState: SheetState,
  onResult: (Boolean) -> Unit,
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
  ) {
    BottomSheetTitleText(isMainCell = isMainCell, isSubCell = isSubCell)
    IconButton(
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .height(21.dp)
        .aspectRatio(1f),
      onClick = {
        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
          if (!bottomSheetState.isVisible) onResult(false)
        }
      },
    ) {
      Icon(
        imageVector = Icons.Default.Clear,
        contentDescription = "Clear Icon",
        tint = Gray900,
      )
    }
  }
}
