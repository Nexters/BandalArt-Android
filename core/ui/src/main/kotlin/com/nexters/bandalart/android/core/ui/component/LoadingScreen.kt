package com.nexters.bandalart.android.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nexters.bandalart.android.core.ui.extension.noRippleClickable
import com.nexters.bandalart.android.core.ui.theme.Black
import com.nexters.bandalart.android.core.ui.theme.White

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier.noRippleClickable { },
    contentAlignment = Alignment.Center,
  ) {
    val composition by rememberLottieComposition(
      spec = LottieCompositionSpec.RawRes(com.nexters.bandalart.android.core.designsystem.R.raw.lottie_loading_animation),
    )
    val progress by animateLottieCompositionAsState(
      composition = composition,
      iterations = 30,
    )

    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Black.copy(alpha = 0.5f)),
      contentAlignment = Alignment.Center,
    ) {
      Card(
        modifier = Modifier
          .height(75.dp)
          .aspectRatio(1f)
          .align(Alignment.Center),
        shape = RoundedCornerShape(90.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(White),
      ) {
        LottieAnimation(
          modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
          composition = composition,
          progress = { progress },
        )
      }
    }
  }
}
