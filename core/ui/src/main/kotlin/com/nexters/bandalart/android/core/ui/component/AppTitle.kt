package com.nexters.bandalart.android.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.designsystem.theme.koronaOneRegular
import com.nexters.bandalart.android.core.designsystem.theme.neurimboGothicRegular
import com.nexters.bandalart.android.core.ui.ComponentPreview
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.util.extension.getCurrentLocale
import java.util.Locale

@Composable
fun AppTitle(
  modifier: Modifier = Modifier,
) {
  val currentLocale = LocalContext.current.getCurrentLocale()

  when (currentLocale.language) {
    Locale.KOREAN.language -> {
      AppKoreanTitle(modifier = modifier)
    }

    Locale.ENGLISH.language -> {
      AppEnglishTitle(modifier = modifier)
    }

    else -> {
      AppEnglishTitle(modifier = modifier)
    }
  }
}

@Composable
fun AppKoreanTitle(
  modifier: Modifier = Modifier,
) {
  FixedSizeText(
    text = stringResource(R.string.bandalart),
    modifier = modifier,
    color = Gray900,
    fontSize = 28.sp,
    fontWeight = FontWeight.W400,
    fontFamily = neurimboGothicRegular,
    lineHeight = 20.sp,
    letterSpacing = (-0.56).sp,
  )
}

@Composable
fun AppEnglishTitle(
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

@ComponentPreview
@Composable
fun AppTitlePreview() {
  AppTitle()
}


@ComponentPreview
@Composable
fun AppKoreanTitlePreview() {
  AppKoreanTitle()
}

@ComponentPreview
@Composable
fun AppEnglishTitlePreview() {
  AppEnglishTitle()
}
