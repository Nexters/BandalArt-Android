package com.nexters.bandalart.android.core.ui.component.bottomsheet

import android.widget.Toast
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.core.ui.theme.Gray200
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.White

@Composable
fun BottomSheetDeleteButton(
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current.applicationContext
  FilledIconButton(
    onClick = { Toast.makeText(context, "삭제 버튼", Toast.LENGTH_SHORT).show() },
    colors = IconButtonColors(Gray200, Gray900, Gray200, Gray900),
    modifier = modifier.height(56.dp),
  ) {
    BottomSheetButtonText(text = "삭제")
  }
}

@Composable
fun BottomSheetCompleteButton(
  modifier: Modifier = Modifier,
  isBlankCell: Boolean,
  onClick: () -> Unit,
) {
  FilledIconButton(
    onClick = onClick,
    colors = IconButtonColors(Gray900, White, Gray200, Gray400),
    modifier = modifier.height(56.dp),
    enabled = !isBlankCell,
  ) {
    BottomSheetButtonText(text = "완료")
  }
}
