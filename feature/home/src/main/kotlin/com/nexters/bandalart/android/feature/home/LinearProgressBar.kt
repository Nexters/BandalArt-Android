package com.nexters.bandalart.android.feature.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.core.ui.theme.Gray100
import com.nexters.bandalart.android.core.ui.theme.Primary

@Composable
fun LinearProgressBar() {
  var progressCount: Int by remember { mutableStateOf(0) }
  var progress by remember { mutableStateOf(0f) }

  /* to avoid the direct calculation of progress variable which is a Float
   and it can sometimes cause problems like it shows 0.4 to 0.400004 so, here I have use
   progressCount and we will increase and decrease it and then convert it to progress(Float)
   and then use that progress with our ProgressBar Width*/
  when (progressCount) {
    0 -> progress = 0.0f
    1 -> progress = 0.1f
    2 -> progress = 0.2f
    3 -> progress = 0.3f
    4 -> progress = 0.4f
    5 -> progress = 0.5f
    6 -> progress = 0.6f
    7 -> progress = 0.7f
    8 -> progress = 0.8f
    9 -> progress = 0.9f
    10 -> progress = 1.0f
  }

  val size by animateFloatAsState(
    targetValue = progress,
    tween(
      durationMillis = 1000,
      delayMillis = 200,
      easing = LinearOutSlowInEasing,
    ),
  )
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentSize(),
  ) {
    // Progress Bar
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(8.dp),
    ) {
      // for the background of the ProgressBar
      Box(
        modifier = Modifier
          .fillMaxSize()
          .clip(RoundedCornerShape(5.dp))
          .background(Gray100),
      )
      // for the progress of the ProgressBar
      Box(
        modifier = Modifier
          .fillMaxWidth(size)
          .fillMaxHeight()
          .clip(RoundedCornerShape(5.dp))
          .background(Primary)
          .animateContentSize(),
      )
    }
  }

  // Use this when you want your progress bar should animate when you open your app
  LaunchedEffect(key1 = true) {
    progressCount = 7
  }
}
