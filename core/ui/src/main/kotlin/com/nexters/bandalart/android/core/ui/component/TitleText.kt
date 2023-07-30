package com.nexters.bandalart.android.core.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.pretendard

@Composable
fun TitleText(
  text: String,
) {
  Text(
    text = text,
    fontFamily = pretendard,
    fontWeight = FontWeight.W700,
    fontSize = 22.sp.nonScaleSp,
    lineHeight = 30.8.sp.nonScaleSp,
    textAlign = TextAlign.Center,
  )
}
