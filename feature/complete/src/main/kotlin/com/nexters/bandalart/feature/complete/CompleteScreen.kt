package com.nexters.bandalart.feature.complete

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nexters.bandalart.core.common.utils.ObserveAsEvents
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray50
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.designsystem.theme.pretendard
import com.nexters.bandalart.core.ui.DevicePreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.component.BandalartButton
import com.nexters.bandalart.feature.complete.ui.CompleteBandalart
import com.nexters.bandalart.feature.complete.ui.CompleteTopBar
import com.nexters.bandalart.feature.complete.viewmodel.CompleteUiAction
import com.nexters.bandalart.feature.complete.viewmodel.CompleteUiEvent
import com.nexters.bandalart.feature.complete.viewmodel.CompleteUiState

// TODO Share 로직 변경 (드로이드카이기 방식으로), 어떻게 구현 해야 할지 고민
@Composable
internal fun CompleteRoute(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CompleteViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(flow = viewModel.uiEvent) { event ->
        when (event) {
            is CompleteUiEvent.NavigateBack -> {
                onNavigateBack()
            }
            is CompleteUiEvent.ShareBandalart -> {
                // 홈 화면에서 완료된 반타라트 비트맵 들고 만들고, 완료 화면에서 공유하는식으로 구현하는건 어떨까?
            }
        }
    }

    CompleteScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@Composable
internal fun CompleteScreen(
    uiState: CompleteUiState,
    onAction: (CompleteUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
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

    LaunchedEffect(key1 = uiState.shareUrl) {
        if (uiState.shareUrl.isNotEmpty()) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    context.getString(R.string.home_share_url, uiState.shareUrl),
                )
                type = context.getString(R.string.home_share_type)
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }

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
                    CompleteTopBar(onNavigateBack = { onAction(CompleteUiAction.OnBackButtonClick) })
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
                        profileEmoji = uiState.profileEmoji,
                        title = uiState.title,
                        modifier = Modifier.width(328.dp),
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    // MVP 제외
                    // SaveImageButton(modifier = Modifier.align(Alignment.BottomCenter))
                    BandalartButton(
                        onClick = { onAction(CompleteUiAction.OnShareButtonClick) },
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
                    CompleteTopBar(onNavigateBack = { onAction(CompleteUiAction.OnBackButtonClick) })
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
                            profileEmoji = uiState.profileEmoji,
                            title = uiState.title,
                            modifier = Modifier.align(Alignment.Center),
                        )
                        // TODO MVP 제외, 이번에 추가해도 좋을듯
                        // SaveImageButton(modifier = Modifier.align(Alignment.BottomCenter))
                        BandalartButton(
                            onClick = { onAction(CompleteUiAction.OnShareButtonClick) },
                            text = stringResource(R.string.complete_share),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 32.dp),
                        )
                    }
                }
            }
        }
    }
}

@DevicePreview
@Composable
private fun CompleteScreenPreview() {
    BandalartTheme {
        CompleteScreen(
            uiState = CompleteUiState(
                id = 0L,
                title = "발전하는 예진",
                profileEmoji = "😎",
                shareUrl = "",
            ),
            onAction = {},
        )
    }
}
