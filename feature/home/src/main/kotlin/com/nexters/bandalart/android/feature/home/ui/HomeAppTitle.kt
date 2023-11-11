package com.nexters.bandalart.android.feature.home.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.designsystem.theme.koronaOneRegular
import com.nexters.bandalart.android.core.designsystem.theme.neurimboGothicRegular
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import java.util.Locale

@Composable
fun HomeAppTitle(
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val currentLocale = context.getCurrentLocale()

  when (currentLocale.language) {
    Locale.KOREAN.language -> {
      HomeAppKoreanTitle(modifier = modifier)
    }

    Locale.ENGLISH.language -> {
      HomeAppEnglishTitle(modifier = modifier)
    }

    else -> {
      HomeAppEnglishTitle(modifier = modifier)
    }
  }
}

@Composable
fun HomeAppKoreanTitle(
  modifier: Modifier = Modifier,
) {
  FixedSizeText(
    modifier = modifier,
    text = stringResource(R.string.bandalart),
    color = Gray900,
    fontSize = 28.sp,
    fontWeight = FontWeight.W400,
    fontFamily = neurimboGothicRegular,
    lineHeight = 20.sp,
    letterSpacing = (-0.56).sp,
  )
}

@Composable
fun HomeAppEnglishTitle(
  modifier: Modifier = Modifier,
) {
  FixedSizeText(
    modifier = modifier,
    text = stringResource(R.string.bandalart),
    color = Gray900,
    fontSize = 18.sp,
    fontWeight = FontWeight.W400,
    fontFamily = koronaOneRegular,
    lineHeight = 20.sp,
    letterSpacing = (-0.36).sp,
  )
}

// TODO core:util 모듈로 옮길 예정
fun Context.getCurrentLocale(): Locale {
  return this.resources.configuration.locales.get(0)
}
