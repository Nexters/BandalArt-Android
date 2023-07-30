@file:OptIn(ExperimentalFoundationApi::class)

package com.nexters.bandalart.android.feature.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nexters.bandalart.android.core.ui.component.BandalartButton
import com.nexters.bandalart.android.core.ui.component.TitleText
import com.nexters.bandalart.android.core.ui.theme.Gray100

@Composable
internal fun OnBoardingRoute(
  onNavigateBack: () -> Unit,
) {
  OnBoardingScreen(
    onNavigateBack = onNavigateBack,
  )
}

@Composable
internal fun OnBoardingScreen(
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.fillMaxSize(),
  ) {
    val pageCount = 2
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Box {
      // TODO status Bar 와의 간격 설정
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
              TitleText(text = "복잡하고 막막한\n만다라트 계획표는 이제 안녕!")
              Spacer(modifier = Modifier.height(50.dp))
              Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.padding(horizontal = 16.dp),
              ) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Gray100),
                  contentAlignment = Alignment.Center,
                ) {
                  val composition by rememberLottieComposition(
                    spec = LottieCompositionSpec.RawRes(com.nexters.bandalart.android.core.designsystem.R.raw.lottie_empty_box),
                  )
                  val progress by animateLottieCompositionAsState(composition)
                  LottieAnimation(
                    composition = composition,
                    progress = { progress },
                  )
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
                TitleText(text = "이제 반다라트와 함께\n부담 없이 계획을 세워봐요")
                Spacer(modifier = Modifier.height(50.dp))
                Card(
                  shape = RoundedCornerShape(16.dp),
                  elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                  modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                  Box(
                    modifier = Modifier
                      .fillMaxWidth()
                      .aspectRatio(1f)
                      .background(Gray100),
                    contentAlignment = Alignment.Center,
                  ) {
                    val composition by rememberLottieComposition(
                      spec = LottieCompositionSpec.RawRes(com.nexters.bandalart.android.core.designsystem.R.raw.lottie_empty_box),
                    )
                    val progress by animateLottieCompositionAsState(composition)
                    LottieAnimation(
                      composition = composition,
                      progress = { progress },
                    )
                  }
                }
              }
              BandalartButton(
                onClick = onNavigateBack,
                text = "시작하기",
                modifier = Modifier
                  .align(Alignment.BottomCenter)
                  // .padding(bottom = 32.dp)
                  .offset(y = (-32).dp),
              )
            }
          }
        }
      }
    }
  }
}
