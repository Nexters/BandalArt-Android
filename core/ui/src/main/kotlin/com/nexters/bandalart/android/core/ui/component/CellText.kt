package com.nexters.bandalart.android.core.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.ui.nonScaleSp
import com.nexters.bandalart.android.core.designsystem.theme.pretendard
import com.nexters.bandalart.android.core.ui.ComponentPreview

val cellLineBreak = LineBreak(
  strategy = LineBreak.Strategy.Simple,
  strictness = LineBreak.Strictness.Normal,
  wordBreak = LineBreak.WordBreak.Phrase,
)

@Composable
fun CellText(
  cellText: String,
  cellTextColor: Color,
  fontWeight: FontWeight,
  modifier: Modifier = Modifier,
  textAlpha: Float = 1f,
) {
  Text(
    text = cellText,
    modifier = modifier
      .padding(horizontal = 4.dp, vertical = 6.dp)
      .alpha(textAlpha),
    color = cellTextColor,
    fontFamily = pretendard,
    fontWeight = fontWeight,
    fontSize = 12.sp.nonScaleSp,
    lineHeight = 16.8.sp.nonScaleSp,
    letterSpacing = (-0.24).sp.nonScaleSp,
    textAlign = TextAlign.Center,
    style = TextStyle(lineBreak = cellLineBreak),
  )
}

@ComponentPreview
@Composable
fun CellTextPreview() {
  CellText(
    cellText = "완벽한 2024년",
    cellTextColor = Gray900,
    fontWeight = FontWeight.W700,
  )
}
