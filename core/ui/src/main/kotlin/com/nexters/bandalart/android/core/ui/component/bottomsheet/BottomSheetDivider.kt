package com.nexters.bandalart.android.core.ui.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.core.designsystem.theme.Gray300

@Composable
fun BottomSheetDivider(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .height(1.dp)
      .fillMaxWidth()
      .background(Gray300),
  )
}
