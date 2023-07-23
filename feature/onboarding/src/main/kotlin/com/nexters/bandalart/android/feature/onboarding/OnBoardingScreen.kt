package com.nexters.bandalart.android.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.NavButton

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
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
    ) {
      Text(
        text = "OnBoarding Screen",
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
      )
      Spacer(modifier = Modifier.height(16.dp))
      Column(
        modifier = Modifier.wrapContentWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        NavButton(
          onClick = onNavigateBack,
          route = "Home",
        )
      }
    }
  }
}
