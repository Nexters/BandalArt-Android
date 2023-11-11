package com.nexters.bandalart.android.core.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.nexters.bandalart.android.core.ui.nonScaleSp
import com.nexters.bandalart.android.core.designsystem.theme.pretendard

@Composable
fun FixedSizeText(
  text: String,
  modifier: Modifier = Modifier,
  color: Color,
  fontSize: TextUnit,
  fontWeight: FontWeight,
  fontFamily: FontFamily = pretendard,
  letterSpacing: TextUnit = TextUnit.Unspecified,
  textAlign: TextAlign? = null,
  lineHeight: TextUnit = TextUnit.Unspecified,
) {
  Text(
    text = text,
    modifier = modifier,
    color = color,
    fontSize = fontSize.nonScaleSp,
    fontWeight = fontWeight,
    fontFamily = fontFamily,
    letterSpacing = letterSpacing.nonScaleSp,
    textAlign = textAlign,
    lineHeight = lineHeight.nonScaleSp,
  )
}
