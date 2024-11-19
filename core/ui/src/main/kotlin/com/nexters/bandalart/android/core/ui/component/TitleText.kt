package com.nexters.bandalart.android.core.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.designsystem.theme.pretendard
import com.nexters.bandalart.android.core.ui.ComponentPreview

@Composable
fun TitleText(
  text: String,
  modifier: Modifier = Modifier,
) {
  Text(
    text = text,
    modifier = modifier,
    color = Gray900,
    fontFamily = pretendard,
    fontWeight = FontWeight.W700,
    fontSize = 22.sp,
    lineHeight = 30.8.sp,
    textAlign = TextAlign.Center,
  )
}

@ComponentPreview
@Composable
fun TitleTextPreview() {
  BandalartTheme {
    TitleText(text = "반다라트의 모든 목표를 달성했어요.\n정말 대단해요!")
  }
}
