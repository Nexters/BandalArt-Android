package com.nexters.bandalart.android.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.pretendard

@Composable
fun BandalartButton(
  onClick: () -> Unit,
  text: String,
  modifier: Modifier = Modifier
) {
  Button(
    onClick = onClick,
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 24.dp)
      .height(56.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = Gray900,
      contentColor = Gray900,
    )
  ) {
    Text(
      text = text,
      fontFamily = pretendard,
      fontWeight = FontWeight.W700,
      fontSize = 16.sp,
      color = Color.White,
    )
  }
}