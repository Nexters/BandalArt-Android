package com.nexters.bandalart.android.feature.complete

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.component.BandalartButton
import com.nexters.bandalart.android.core.ui.component.TitleText
import com.nexters.bandalart.android.feature.complete.ui.CompleteBandalart
import com.nexters.bandalart.android.feature.complete.ui.CompleteTopBar

@Composable
internal fun CompleteRoute(
  modifier: Modifier = Modifier,
  onNavigateBack: () -> Unit,
  viewModel: CompleteViewModel = hiltViewModel(),
) {
  val context = LocalContext.current
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(viewModel) {
    viewModel.eventFlow.collect { event ->
      when (event) {
        is CompleteUiEvent.ShowToast -> {
          Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  CompleteScreen(
    modifier = modifier,
    uiState = uiState,
    onNavigateBack = onNavigateBack,
    shareBandalart = viewModel::shareBandalart,
    initShareUrl = viewModel::initShareUrl,
  )
}

@Composable
internal fun CompleteScreen(
  modifier: Modifier = Modifier,
  uiState: CompleteUiState,
  onNavigateBack: () -> Unit,
  shareBandalart: () -> Unit,
  initShareUrl: () -> Unit,
) {
  val context = LocalContext.current
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
      initShareUrl()
    }
  }

  Surface(
    modifier = modifier.fillMaxSize(),
  ) {
    Box {
      LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.align(Alignment.TopCenter),
      )
      Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Spacer(modifier = Modifier.height(16.dp))
        CompleteTopBar(
          context = context,
          onNavigateBack = onNavigateBack,
        )
        Spacer(modifier = Modifier.height(40.dp))
        TitleText(text = context.getString(R.string.complete_title))
        Box(modifier = Modifier.fillMaxSize()) {
          CompleteBandalart(
            modifier = Modifier.align(Alignment.Center),
            uiState = uiState,
          )
          // MVP 제외
          // SaveImageButton(modifier = Modifier.align(Alignment.BottomCenter))
          BandalartButton(
            onClick = shareBandalart,
            text = context.getString(R.string.complete_share),
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .padding(bottom = 32.dp),
          )
        }
      }
    }
  }
}
