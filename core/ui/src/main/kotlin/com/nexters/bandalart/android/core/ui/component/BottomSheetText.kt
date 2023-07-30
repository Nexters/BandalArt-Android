package com.nexters.bandalart.android.core.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray600
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.pretendard

@Composable
fun BottomSheetTitleText(
  modifier: Modifier = Modifier,
  isMainCell: Boolean,
  isSubCell: Boolean,
) {
  Text(
    text = if (isMainCell) "메인 목표 수정" else if (isSubCell) "서브 목표 수정" else "태스크 수정",
    modifier = modifier
      .fillMaxWidth()
      .padding(top = 20.dp),
    textAlign = TextAlign.Center,
    color = Gray900,
    fontSize = 16.sp,
    fontFamily = pretendard,
    fontWeight = FontWeight.W700,
  )
}

@Composable
fun BottomSheetSubTitleText(
  modifier: Modifier = Modifier,
  text: String,
) {
  Text(
    modifier = modifier,
    text = text,
    textAlign = TextAlign.Start,
    color = Gray600,
    fontSize = 12.sp,
    fontFamily = pretendard,
    fontWeight = FontWeight.W700,
  )
}

@Composable
fun BottomSheetContentText(
  modifier: Modifier = Modifier,
  text: String,
) {
  Text(
    modifier = modifier,
    text = text,
    color = Gray400,
    fontSize = 16.sp,
    fontFamily = pretendard,
    fontWeight = FontWeight.W600,
  )
}

@Composable
fun BottomSheetButtonText(
  modifier: Modifier = Modifier,
  text: String,
) {
  Text(
    modifier = modifier,
    text = text,
    fontSize = 16.sp,
    fontFamily = pretendard,
    fontWeight = FontWeight.W700,
  )
}
