package com.nexters.bandalart.core.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.designsystem.theme.koronaOneRegular
import com.nexters.bandalart.core.designsystem.theme.neurimboGothicRegular
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.common.extension.getCurrentLocale
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
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
    Text(
        text = stringResource(R.string.bandalart),
        color = Gray900,
        fontSize = 28.sp,
        fontWeight = FontWeight.W400,
        modifier = modifier,
        fontFamily = neurimboGothicRegular,
        lineHeight = 20.sp,
        letterSpacing = (-0.56).sp,
    )
}

@Composable
fun AppEnglishTitle(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.bandalart),
        color = Gray900,
        fontSize = 18.sp,
        fontWeight = FontWeight.W400,
        modifier = modifier,
        fontFamily = koronaOneRegular,
        lineHeight = 20.sp,
        letterSpacing = (-0.36).sp,
    )
}

@ComponentPreview
@Composable
private fun AppTitlePreview() {
    BandalartTheme {
        AppTitle()
    }
}


@ComponentPreview
@Composable
private fun AppKoreanTitlePreview() {
    BandalartTheme {
        AppKoreanTitle()
    }
}

@ComponentPreview
@Composable
private fun AppEnglishTitlePreview() {
    BandalartTheme {
        AppEnglishTitle()
    }
}
