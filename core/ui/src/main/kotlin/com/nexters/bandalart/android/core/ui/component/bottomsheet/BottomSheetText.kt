package com.nexters.bandalart.android.core.ui.component.bottomsheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray600
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.pretendard

// TODO 각각이 비어 있는 경우 수정이 아닌 입력이 되어야
@Composable
fun BottomSheetTitleText(
  modifier: Modifier = Modifier,
  isMainCell: Boolean,
  isSubCell: Boolean,
) {
  Text(
    text = if (isMainCell) "메인 목표 수정" else if (isSubCell) "서브 목표 수정" else "태스크 수정",
    modifier = modifier.fillMaxWidth(),
    textAlign = TextAlign.Center,
    color = Gray900,
    fontSize = 16.sp.nonScaleSp,
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
    fontSize = 12.sp.nonScaleSp,
    fontFamily = pretendard,
    fontWeight = FontWeight.W700,
  )
}

@Composable
fun BottomSheetContentText(
  modifier: Modifier = Modifier,
  text: String,
  color: Color = Gray400
) {
  Text(
    modifier = modifier,
    text = text,
    color = color,
    fontFamily = pretendard,
    fontWeight = FontWeight.W600,
    fontSize = 16.sp.nonScaleSp,
    letterSpacing = -(0.32).sp.nonScaleSp,
    lineHeight = 22.4.sp.nonScaleSp,
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
    fontSize = 16.sp.nonScaleSp,
    fontFamily = pretendard,
    fontWeight = FontWeight.W700,
  )
}
