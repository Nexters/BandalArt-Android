package com.nexters.bandalart.android.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.extension.clickableSingle
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.White
import com.nexters.bandalart.android.core.ui.theme.pretendard

@Composable
fun BandalartButton(
  onClick: () -> Unit,
  text: String,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 24.dp)
      .height(56.dp)
      .clip(shape = RoundedCornerShape(50.dp))
      .clickableSingle(onClick = onClick)
      .background(color = Gray900)
      .padding(16.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      fontFamily = pretendard,
      fontWeight = FontWeight.W700,
      fontSize = 16.sp.nonScaleSp,
      color = White,
      letterSpacing = (-0.32).sp.nonScaleSp,
    )
  }
}
