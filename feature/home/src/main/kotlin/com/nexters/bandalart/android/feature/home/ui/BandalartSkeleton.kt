package com.nexters.bandalart.android.feature.home.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.nexters.bandalart.android.core.ui.theme.Gray100
import com.nexters.bandalart.android.core.ui.theme.Gray200
import com.nexters.bandalart.android.core.ui.theme.Gray300

@Composable
fun BandalartSkeleton() {
  val shimmerColors = listOf(
    Gray100,
    Gray200,
    Gray300,
  )

  val transition = rememberInfiniteTransition(label = "Skeleton transition")
  val translateAnim = transition.animateFloat(
    initialValue = 0f,
    targetValue = 400f,
    animationSpec = infiniteRepeatable(
      animation = tween(
        durationMillis = 700,
        easing = FastOutSlowInEasing,
      ),
      repeatMode = RepeatMode.Reverse,
    ),
    label = "Skeleton translateAnim",
  )

  val brush = Brush.linearGradient(
    colors = shimmerColors,
    start = Offset.Zero,
    end = Offset(x = translateAnim.value, y = translateAnim.value),
  )

  BandalartSkeletonScreen(brush = brush)
}
