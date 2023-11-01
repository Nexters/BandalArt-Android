@file:OptIn(ExperimentalFoundationApi::class)

package com.nexters.bandalart.android.feature.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.component.BandalartButton
import com.nexters.bandalart.android.core.ui.component.TitleText
import com.nexters.bandalart.android.core.designsystem.theme.Gray50
import com.nexters.bandalart.android.feature.onboarding.navigation.ONBOARDING_NAVIGATION_ROUTE

@Composable
internal fun OnBoardingRoute(
  navigateToHome: (NavOptions) -> Unit,
) {
  OnBoardingScreen(navigateToHome = navigateToHome)
}

@Composable
internal fun OnBoardingScreen(
  modifier: Modifier = Modifier,
  navigateToHome: (NavOptions) -> Unit,
) {
  val composition by rememberLottieComposition(
    spec = LottieCompositionSpec.RawRes(
      com.nexters.bandalart.android.core.designsystem.R.raw.lottie_onboarding,
    ),
  )
  val progress by animateLottieCompositionAsState(
    composition = composition,
    iterations = LottieConstants.IterateForever,
  )
  val context = LocalContext.current

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
              TitleText(text = context.getString(R.string.onboarding_first_title))
              Spacer(modifier = Modifier.height(50.dp))
              Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
              ) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Gray50),
                  contentAlignment = Alignment.Center,
                ) {
                  Image(
                    painter = painterResource(com.nexters.bandalart.android.core.designsystem.R.drawable.ic_onboarding),
                    contentDescription = context.getString(R.string.delete_descrption),
                    modifier = Modifier.fillMaxSize(),
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
                TitleText(text = context.getString(R.string.onboarding_second_title))
                Spacer(modifier = Modifier.height(50.dp))
                Card(
                  shape = RoundedCornerShape(16.dp),
                  modifier = Modifier.padding(16.dp),
                ) {
                  Box(
                    modifier = Modifier
                      .fillMaxWidth()
                      .aspectRatio(1f)
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
              BandalartButton(
                onClick = {
                  val options = NavOptions.Builder()
                    .setPopUpTo(ONBOARDING_NAVIGATION_ROUTE, inclusive = true)
                    .build()
                  navigateToHome(options)
                },
                text = context.getString(R.string.onboarding_start),
                modifier = Modifier
                  .align(Alignment.BottomCenter)
                  .padding(bottom = 32.dp),
              )
            }
          }
        }
      }
    }
  }
}
