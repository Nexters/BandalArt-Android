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
import com.nexters.bandalart.android.core.ui.nonScaleSp
import com.nexters.bandalart.android.core.designsystem.theme.pretendard

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
    modifier = modifier
      .padding(horizontal = 4.dp, vertical = 6.dp)
      .alpha(textAlpha),
    text = cellText,
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
