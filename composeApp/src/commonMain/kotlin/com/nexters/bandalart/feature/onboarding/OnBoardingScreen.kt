package com.nexters.bandalart.feature.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavOptions
import bandalart_android.composeapp.generated.resources.Res
import com.nexters.bandalart.core.common.extension.aspectRatioBasedOnOrientation
import com.nexters.bandalart.core.common.extension.getCurrentLocale
import com.nexters.bandalart.core.common.utils.ObserveAsEvents
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray50
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.designsystem.theme.pretendard
import com.nexters.bandalart.core.navigation.Route
import com.nexters.bandalart.core.ui.DevicePreview
import com.nexters.bandalart.core.ui.component.BandalartButton
import com.nexters.bandalart.core.ui.component.LottieImage
import com.nexters.bandalart.core.ui.component.PagerIndicator
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale

@Composable
internal fun OnBoardingRoute(
    navigateToHome: (NavOptions) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    ObserveAsEvents(flow = viewModel.uiEvent) { event ->
        when (event) {
            is OnBoardingUiEvent.NavigateToHome -> {
                val options = NavOptions.Builder()
                    .setPopUpTo(Route.Onboarding, inclusive = true)
                    .build()
                navigateToHome(options)
            }
        }
    }

    OnBoardingScreen(
        setOnboardingCompletedStatus = viewModel::setOnboardingCompletedStatus,
        modifier = modifier,
    )
}

@Composable
internal fun OnBoardingScreen(
    setOnboardingCompletedStatus: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val currentLocale = context.getCurrentLocale()
    val configuration = LocalConfiguration.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Gray50,
    ) {
        val pageCount = 2
        val pagerState = rememberPagerState(pageCount = { pageCount })

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            PagerIndicator(
                pageCount = pageCount,
                pagerState = pagerState,
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                when (page) {
                    0 -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(modifier = Modifier.height(50.dp))
                            Text(
                                text = stringResource(Res.string.onboarding_first_title),
                                modifier = modifier,
                                color = Gray900,
                                fontFamily = pretendard,
                                fontWeight = FontWeight.W700,
                                fontSize = 22.sp,
                                lineHeight = 30.8.sp,
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.padding(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .aspectRatioBasedOnOrientation(1f)
                                        .background(Gray50),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    when (currentLocale.language) {
                                        Locale.KOREAN.language -> {
                                            Image(
                                                imageVector = vectorResource(Res.drawable.ic_onboarding_kr),
                                                contentDescription = stringResource(Res.string.delete_description),
                                                modifier = Modifier.fillMaxSize(),
                                            )
                                        }

                                        Locale.ENGLISH.language -> {
                                            Image(
                                                imageVector = vectorResource(Res.drawable.ic_onboarding_en),
                                                contentDescription = stringResource(Res.string.delete_description),
                                                modifier = Modifier.fillMaxSize(),
                                            )
                                        }

                                        Locale.JAPANESE.language -> {
                                            Image(
                                                imageVector = vectorResource(Res.drawable.ic_onboarding_jp),
                                                contentDescription = stringResource(Res.string.delete_description),
                                                modifier = Modifier.fillMaxSize(),
                                            )
                                        }

                                        else -> {
                                            Image(
                                                imageVector = vectorResource(Res.drawable.ic_onboarding_en),
                                                contentDescription = stringResource(Res.string.delete_description),
                                                modifier = Modifier.fillMaxSize(),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        Box {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Spacer(modifier = Modifier.height(50.dp))
                                Text(
                                    text = stringResource(Res.string.onboarding_second_title),
                                    modifier = modifier,
                                    color = Gray900,
                                    fontFamily = pretendard,
                                    fontWeight = FontWeight.W700,
                                    fontSize = 22.sp,
                                    lineHeight = 30.8.sp,
                                    textAlign = TextAlign.Center,
                                )
                                Spacer(modifier = Modifier.height(50.dp))
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.padding(16.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .aspectRatioBasedOnOrientation(1f)
                                            .background(Gray50),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        LottieImage(
                                            jsonString = when (currentLocale.language) {
                                                Locale.KOREAN.language -> Res.raw.lottie_onboarding_kr
                                                Locale.ENGLISH.language -> Res.raw.lottie_onboarding_en
                                                Locale.JAPANESE.language -> Res.raw.lottie_onboarding_jp
                                                else -> Res.raw.lottie_onboarding_en
                                            },
                                            iterations = Int.MAX_VALUE,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                BandalartButton(
                                    onClick = { setOnboardingCompletedStatus(true) },
                                    text = stringResource(Res.string.onboarding_start),
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .align(Alignment.BottomEnd)
                                        .padding(bottom = 32.dp, start = 24.dp, end = 24.dp)
                                        .clip(shape = RoundedCornerShape(50.dp))
                                        .background(Gray900),
                                )
                            } else {
                                BandalartButton(
                                    onClick = { setOnboardingCompletedStatus(true) },
                                    text = stringResource(Res.string.onboarding_start),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 32.dp, start = 24.dp, end = 24.dp)
                                        .clip(shape = RoundedCornerShape(50.dp))
                                        .background(Gray900),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@DevicePreview
@Composable
private fun OnBoardingScreenPreview() {
    BandalartTheme {
        OnBoardingScreen(
            setOnboardingCompletedStatus = {},
        )
    }
}
