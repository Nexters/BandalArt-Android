package com.nexters.bandalart.feature.complete

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray50
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.designsystem.theme.pretendard
import com.nexters.bandalart.core.ui.DevicePreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.component.BandalartButton
import com.nexters.bandalart.feature.complete.ui.CompleteBandalart
import com.nexters.bandalart.feature.complete.ui.CompleteTopBar
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompleteScreen(
    val bandalartId: Long,
    val bandalartTitle: String,
    val bandalartProfileEmoji: String,
    val bandalartChartImageUri: String,
) : Screen {
    data class State(
        val id: Long = 0L,
        val title: String = "",
        val profileEmoji: String = "",
        val isShared: Boolean = false,
        val bandalartChartImageUri: String = "",
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object NavigateBack : Event
        data class SaveBandalart(val imageUri: Uri) : Event
        data class ShareBandalart(val imageUri: Uri) : Event
    }
}

@CircuitInject(CompleteScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Complete(
    state: CompleteScreen.State,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val configuration = LocalConfiguration.current

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            com.nexters.bandalart.core.designsystem.R.raw.lottie_finish,
        ),
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Gray50,
    ) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CompleteTopBar(
                        onNavigateBack = {
                            eventSink(CompleteScreen.Event.NavigateBack)
                        },
                    )
                    Text(
                        text = stringResource(R.string.complete_title),
                        modifier = modifier,
                        color = Gray900,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.W700,
                        fontSize = 22.sp,
                        lineHeight = 30.8.sp,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(text = "🥳", fontSize = 100.sp)
                    Spacer(modifier = Modifier.height(32.dp))
                    CompleteBandalart(
                        profileEmoji = state.profileEmoji,
                        title = state.title,
                        // bandalartChartImageUri = uiState.bandalartChartImageUri,
                        modifier = Modifier.width(328.dp),
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    BandalartButton(
                        onClick = {
                            eventSink(CompleteScreen.Event.SaveBandalart(Uri.parse(state.bandalartChartImageUri)))
                        },
                        text = stringResource(R.string.complete_save),
                        modifier = Modifier
                            .width(328.dp)
                            .padding(bottom = 32.dp),
                    )
                    BandalartButton(
                        onClick = {
                            eventSink(CompleteScreen.Event.ShareBandalart(Uri.parse(state.bandalartChartImageUri)))
                        },
                        text = stringResource(R.string.complete_share),
                        modifier = Modifier
                            .width(328.dp)
                            .padding(bottom = 32.dp),
                    )
                }
            }
        } else {
            Box {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.align(Alignment.TopCenter),
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CompleteTopBar(
                        onNavigateBack = {
                            eventSink(CompleteScreen.Event.NavigateBack)
                        },
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = stringResource(R.string.complete_title),
                        modifier = modifier,
                        color = Gray900,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.W700,
                        fontSize = 22.sp,
                        lineHeight = 30.8.sp,
                        textAlign = TextAlign.Center,
                    )
                    Box(modifier = Modifier.fillMaxSize()) {
                        CompleteBandalart(
                            profileEmoji = state.profileEmoji,
                            title = state.title,
                            // bandalartChartImageUri = uiState.bandalartChartImageUri,
                            modifier = Modifier.align(Alignment.Center),
                        )
                        Column(
                            modifier = Modifier.align(Alignment.BottomCenter),
                        ) {
                            BandalartButton(
                                onClick = {
                                    eventSink(CompleteScreen.Event.SaveBandalart(Uri.parse(state.bandalartChartImageUri)))
                                },
                                text = stringResource(R.string.complete_save),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 8.dp)
                                    .clip(shape = RoundedCornerShape(50.dp))
                                    .background(Gray900),
                            )
                            BandalartButton(
                                onClick = {
                                    eventSink(CompleteScreen.Event.ShareBandalart(Uri.parse(state.bandalartChartImageUri)))
                                },
                                text = stringResource(R.string.complete_share),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 32.dp, start = 24.dp, end = 24.dp)
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

@DevicePreview
@Composable
private fun CompletePreview() {
    BandalartTheme {
        Complete(
            state = CompleteScreen.State(
                id = 0L,
                title = "발전하는 예진",
                profileEmoji = "😎",
                eventSink = {},
            ),
        )
    }
}
