package com.nexters.bandalart.android.core.ui.component.bottomsheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray600
import com.nexters.bandalart.android.core.ui.theme.Gray900

// TODO 각각이 비어 있는 경우 수정이 아닌 입력이 되어야
@Composable
fun BottomSheetTitleText(
  modifier: Modifier = Modifier,
  isMainCell: Boolean,
  isSubCell: Boolean,
  isBlankCell: Boolean,
) {
  FixedSizeText(
    text =
    if (isBlankCell)
      if (isMainCell) "메인목표 입력"
      else if (isSubCell) "서브목표 입력"
      else "태스크 입력"
    else if (isMainCell) "메인목표 수정"
    else if (isSubCell) "서브목표 수정"
    else "태스크 수정",
    modifier = modifier.fillMaxWidth(),
    textAlign = TextAlign.Center,
    color = Gray900,
    fontSize = 16.sp,
    fontWeight = FontWeight.W700,
    letterSpacing = (-0.32).sp,
    lineHeight = 22.4.sp,
  )
}

@Composable
fun BottomSheetSubTitleText(
  modifier: Modifier = Modifier,
  text: String,
) {
  FixedSizeText(
    modifier = modifier,
    text = text,
    textAlign = TextAlign.Start,
    color = Gray600,
    fontSize = 12.sp,
    fontWeight = FontWeight.W700,
    letterSpacing = (-0.24).sp,
    lineHeight = 22.4.sp,
  )
}

@Composable
fun BottomSheetContentPlaceholder(
  modifier: Modifier = Modifier,
  text: String,
) {
  FixedSizeText(
    modifier = modifier,
    text = text,
    textAlign = TextAlign.Start,
    color = Gray400,
    fontSize = 16.sp,
    fontWeight = FontWeight.W400,
    letterSpacing = (-0.32).sp,
    lineHeight = 22.4.sp,
  )
}

@Composable
fun BottomSheetContentText(
  modifier: Modifier = Modifier,
  text: String,
) {
  FixedSizeText(
    modifier = modifier,
    text = text,
    textAlign = TextAlign.Start,
    color = Gray600,
    fontSize = 16.sp,
    fontWeight = FontWeight.W600,
    letterSpacing = (-0.32).sp,
    lineHeight = 22.4.sp,
  )
}

@Composable
fun BottomSheetButtonText(
  modifier: Modifier = Modifier,
  text: String,
  color: Color,
) {
  FixedSizeText(
    modifier = modifier,
    text = text,
    textAlign = TextAlign.Start,
    color = color,
    fontSize = 16.sp,
    fontWeight = FontWeight.W700,
    letterSpacing = (-0.32).sp,
    lineHeight = 21.sp,
  )
}
