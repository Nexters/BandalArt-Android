package com.nexters.bandalart.android.core.ui.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.core.ui.theme.Gray300

@Composable
fun BottomSheetSpacer() {
  Spacer(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 10.dp)
      .height(1.dp)
      .background(color = Gray300),
  )
}
