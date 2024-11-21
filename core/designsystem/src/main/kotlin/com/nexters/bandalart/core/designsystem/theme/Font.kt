package com.nexters.bandalart.core.designsystem.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.core.designsystem.R

val pretendard = FontFamily(
    Font(R.font.pretendard_black, FontWeight.Black, FontStyle.Normal),
    Font(R.font.pretendard_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.pretendard_extra_bold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.pretendard_extra_light, FontWeight.ExtraLight, FontStyle.Normal),
    Font(R.font.pretendard_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.pretendard_thin, FontWeight.Thin, FontStyle.Normal),
)

val neurimboGothicRegular = FontFamily(
    Font(R.font.neurimbo_gothic_regular, FontWeight.Normal, FontStyle.Normal),
)

val koronaOneRegular = FontFamily(
    Font(R.font.krona_one_regular, FontWeight.Normal, FontStyle.Normal),
)

val BottomSheetContent = TextStyle(
    color = Gray900,
    fontFamily = pretendard,
    fontWeight = FontWeight.W600,
    fontSize = 16.sp,
    letterSpacing = -(0.32).sp,
    lineHeight = 22.4.sp,
)
