package com.nexters.bandalart.android.core.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.ui.nonScaleSp
import com.nexters.bandalart.android.core.designsystem.theme.pretendard
import com.nexters.bandalart.android.core.ui.ComponentPreview

@Composable
fun FixedSizeText(
  text: String,
  color: Color,
  fontSize: TextUnit,
  fontWeight: FontWeight,
  modifier: Modifier = Modifier,
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

@ComponentPreview
@Composable
fun FixedSizeTextPreview() {
  FixedSizeText(
    text = "발전하는 예진",
    color = Gray900,
    fontSize = 16.sp,
    fontWeight = FontWeight.W700,
  )
}
