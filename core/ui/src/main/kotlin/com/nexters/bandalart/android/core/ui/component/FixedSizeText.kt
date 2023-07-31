package com.nexters.bandalart.android.core.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.pretendard

@Composable
fun FixedSizeText(
  modifier: Modifier = Modifier,
  text: String,
  color: Color,
  fontWeight: FontWeight,
  fontSize: TextUnit,
  letterSpacing: TextUnit = TextUnit.Unspecified,
  textAlign: TextAlign? = null,
) {
  Text(
    modifier = modifier,
    text = text,
    color = color,
    fontFamily = pretendard,
    fontWeight = fontWeight,
    fontSize = fontSize.nonScaleSp,
    letterSpacing = letterSpacing.nonScaleSp,
    textAlign = textAlign,
  )
}
