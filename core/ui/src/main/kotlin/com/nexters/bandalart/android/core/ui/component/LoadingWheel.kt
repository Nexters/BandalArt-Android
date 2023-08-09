package com.nexters.bandalart.android.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// TODO 다른 로띠로 변경
@Composable
fun LoadingWheel(
  progressColor: Color,
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(1f)
      .background(Color.Transparent),
  ) {
    CircularProgressIndicator(
      modifier = Modifier
        .align(Alignment.Center)
        .size(30.dp),
      color = progressColor,
    )
  }
}
