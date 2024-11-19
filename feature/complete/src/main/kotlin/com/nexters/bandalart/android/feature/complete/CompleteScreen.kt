package com.nexters.bandalart.android.feature.complete

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nexters.bandalart.android.core.common.ObserveAsEvents
import com.nexters.bandalart.android.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.android.core.designsystem.theme.Gray50
import com.nexters.bandalart.android.core.ui.DevicePreview
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.component.BandalartButton
import com.nexters.bandalart.android.core.ui.component.EmojiText
import com.nexters.bandalart.android.core.ui.component.TitleText
import com.nexters.bandalart.android.feature.complete.ui.CompleteBandalart
import com.nexters.bandalart.android.feature.complete.ui.CompleteTopBar

// TODO Share 로직 변경 (드로이드카이기 방식으로), 어떻게 구현 해야 할지 고민
@Composable
internal fun CompleteRoute(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CompleteViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(flow = viewModel.uiEvent) { event ->
        when (event) {
            is CompleteUiEvent.NavigateToHome -> {
                onNavigateBack()
            }
        }
    }

    CompleteScreen(
        uiState = uiState,
        navigateToHome = viewModel::navigateToHome,
        shareBandalart = viewModel::shareBandalart,
        modifier = modifier,
    )
}

@Composable
internal fun CompleteScreen(
    uiState: CompleteUiState,
    navigateToHome: () -> Unit,
    shareBandalart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            com.nexters.bandalart.android.core.designsystem.R.raw.lottie_finish,
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
                    CompleteTopBar(onNavigateBack = navigateToHome)
                    TitleText(text = context.getString(R.string.complete_title))
                    Spacer(modifier = Modifier.height(32.dp))
                    EmojiText(emojiText = "🥳", fontSize = 100.sp)
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
                        onClick = shareBandalart,
                        text = context.getString(R.string.complete_share),
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
                    CompleteTopBar(onNavigateBack = navigateToHome)
                    Spacer(modifier = Modifier.height(40.dp))
                    TitleText(text = context.getString(R.string.complete_title))
                    Box(modifier = Modifier.fillMaxSize()) {
                        CompleteBandalart(
                            profileEmoji = uiState.profileEmoji,
                            title = uiState.title,
                            modifier = Modifier.align(Alignment.Center),
                        )
                        // TODO MVP 제외, 이번에 추가해도 좋을듯
                        // SaveImageButton(modifier = Modifier.align(Alignment.BottomCenter))
                        BandalartButton(
                            onClick = shareBandalart,
                            text = context.getString(R.string.complete_share),
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
            navigateToHome = {},
            shareBandalart = {},
        )
    }
}
