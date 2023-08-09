package com.nexters.bandalart.android.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LoadingWheel(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    val composition by rememberLottieComposition(
      spec = LottieCompositionSpec.RawRes(com.nexters.bandalart.android.core.designsystem.R.raw.lottie_loading_animation),
    )
    val progress by animateLottieCompositionAsState(
      composition = composition,
      iterations = 30,
    )
    LottieAnimation(
      composition = composition,
      progress = { progress },
    )
  }
}
