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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nexters.bandalart.core.common.extension.aspectRatioBasedOnOrientation
import com.nexters.bandalart.core.common.extension.getCurrentLocale
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray50
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.designsystem.theme.pretendard
import com.nexters.bandalart.core.ui.DevicePreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.component.BandalartButton
import com.nexters.bandalart.core.ui.component.PagerIndicator
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.android.components.ActivityRetainedComponent
import com.nexters.bandalart.feature.onboarding.OnboardingScreen.Event
import kotlinx.parcelize.Parcelize
import java.util.Locale
import com.nexters.bandalart.core.designsystem.R as DesignR

@Parcelize
data object OnboardingScreen : Screen {
    data class State(
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object NavigateToHome : Event
    }
}

@CircuitInject(OnboardingScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Onboarding(
    state: OnboardingScreen.State,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val context = LocalContext.current
    val currentLocale = context.getCurrentLocale()
    val configuration = LocalConfiguration.current

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            when (currentLocale.language) {
                Locale.KOREAN.language -> {
                    com.nexters.bandalart.core.designsystem.R.raw.lottie_onboarding_kr
                }

                Locale.ENGLISH.language -> {
                    com.nexters.bandalart.core.designsystem.R.raw.lottie_onboarding_en
                }

                else -> {
                    com.nexters.bandalart.core.designsystem.R.raw.lottie_onboarding_en
                }
            },
        ),
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Gray50),
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
                                text = stringResource(R.string.onboarding_first_title),
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
                                                imageVector = ImageVector.vectorResource(DesignR.drawable.ic_onboarding_kr),
                                                contentDescription = stringResource(R.string.delete_description),
                                                modifier = Modifier.fillMaxSize(),
                                            )
                                        }

                                        Locale.ENGLISH.language -> {
                                            Image(
                                                imageVector = ImageVector.vectorResource(DesignR.drawable.ic_onboarding_en),
                                                contentDescription = stringResource(R.string.delete_description),
                                                modifier = Modifier.fillMaxSize(),
                                            )
                                        }

                                        else -> {
                                            Image(
                                                imageVector = ImageVector.vectorResource(DesignR.drawable.ic_onboarding_en),
                                                contentDescription = stringResource(R.string.delete_description),
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
                                    text = stringResource(R.string.onboarding_second_title),
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
                                        LottieAnimation(
                                            composition = composition,
                                            progress = { progress },
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                    }
                                }
                            }
                            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                BandalartButton(
                                    onClick = {
                                        eventSink(Event.NavigateToHome)
                                    },
                                    text = stringResource(R.string.onboarding_start),
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .align(Alignment.BottomEnd)
                                        .padding(bottom = 32.dp, start = 24.dp, end = 24.dp)
                                        .clip(shape = RoundedCornerShape(50.dp))
                                        .background(Gray900),
                                )
                            } else {
                                BandalartButton(
                                    onClick = {
                                        eventSink(Event.NavigateToHome)
                                    },
                                    text = stringResource(R.string.onboarding_start),
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
private fun OnBoardingPreview() {
    BandalartTheme {
        Onboarding(
            state = OnboardingScreen.State(
                eventSink = {},
            ),
        )
    }
}
