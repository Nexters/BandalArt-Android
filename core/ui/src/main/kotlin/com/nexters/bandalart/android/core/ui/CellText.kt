package com.nexters.bandalart.android.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.pretendard

@Composable
fun CellText(
  modifier: Modifier = Modifier,
  cellText: String,
  cellColor: Color,
  fontWeight: FontWeight,
) {
  Text(
    modifier = modifier.padding(horizontal = 4.dp, vertical = 6.dp),
    text = cellText,
    color = cellColor,
    fontFamily = pretendard,
    fontWeight = fontWeight,
    fontSize = 12.sp.nonScaleSp,
    lineHeight = 16.8.sp.nonScaleSp,
    letterSpacing = (-0.24).sp.nonScaleSp,
    textAlign = TextAlign.Center,
  )
}
