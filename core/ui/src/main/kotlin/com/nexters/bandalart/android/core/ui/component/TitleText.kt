package com.nexters.bandalart.android.core.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.nonScaleSp
import com.nexters.bandalart.android.core.designsystem.theme.pretendard

@Composable
fun TitleText(
  text: String,
  modifier: Modifier = Modifier,
) {
  Text(
    text = text,
    modifier = modifier,
    fontFamily = pretendard,
    fontWeight = FontWeight.W700,
    fontSize = 22.sp.nonScaleSp,
    lineHeight = 30.8.sp.nonScaleSp,
    textAlign = TextAlign.Center,
  )
}
