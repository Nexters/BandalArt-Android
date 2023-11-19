package com.nexters.bandalart.android.core.ui.component.bottomsheet

import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.core.designsystem.theme.Gray200
import com.nexters.bandalart.android.core.designsystem.theme.Gray400
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.designsystem.theme.White
import com.nexters.bandalart.android.core.ui.R

@Composable
fun BottomSheetDeleteButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  FilledIconButton(
    onClick = onClick,
    colors = IconButtonColors(Gray200, Gray900, Gray200, Gray900),
    modifier = modifier.height(56.dp),
  ) {
    BottomSheetButtonText(
      text = stringResource(R.string.bottomsheet_delete),
      color = Gray900,
    )
  }
}

@Composable
fun BottomSheetCompleteButton(
  isBlankCell: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  FilledIconButton(
    onClick = onClick,
    colors = IconButtonColors(Gray900, White, Gray200, Gray400),
    modifier = modifier.height(56.dp),
    enabled = !isBlankCell,
  ) {
    BottomSheetButtonText(
      text = stringResource(R.string.bottomsheet_done),
      color = if (isBlankCell) Gray400 else White,
    )
  }
}
