package com.nexters.bandalart.android.core.ui.component.bottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.designsystem.theme.pretendard

@Composable
fun BottomSheetTextStyle() = TextStyle(
  color = Gray900,
  fontFamily = pretendard,
  fontWeight = FontWeight.W600,
  fontSize = 16.sp,
  letterSpacing = -(0.32).sp,
  lineHeight = 22.4.sp,
)
