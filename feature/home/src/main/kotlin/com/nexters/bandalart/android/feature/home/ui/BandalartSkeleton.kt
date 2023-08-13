package com.nexters.bandalart.android.feature.home.ui

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import com.nexters.bandalart.android.core.ui.theme.Gray100
import com.nexters.bandalart.android.core.ui.theme.Gray200
import com.nexters.bandalart.android.core.ui.theme.Gray300
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray50
import com.nexters.bandalart.android.core.ui.theme.White
import com.nexters.bandalart.android.core.ui.R

@Composable
fun BandalartSkeleton() {
  val context = LocalContext.current
  val shimmerMainColors = listOf(
    Gray200,
    Gray300,
    Gray400,
  )
  val shimmerSubColors = listOf(
    Gray100,
    Gray200,
    Gray300,
  )
  val shimmerTaskColors = listOf(
    White,
    Gray50,
  )
  val transition = rememberInfiniteTransition(label = "Skeleton transition")
  val translateAnim = transition.animateFloat(
    initialValue = 0f,
    targetValue = 800f,
    animationSpec = infiniteRepeatable(
      animation = tween(
        durationMillis = 600,
        easing = FastOutLinearInEasing,
      ),
      repeatMode = RepeatMode.Reverse,
    ),
    label = context.getString(R.string.skeleton_trans_animate_label_text),
  )
  val mainBrush = Brush.linearGradient(
    colors = shimmerMainColors,
    start = Offset.Zero,
    end = Offset(x = translateAnim.value, y = translateAnim.value),
  )
  val subBrush = Brush.linearGradient(
    colors = shimmerSubColors,
    start = Offset.Zero,
    end = Offset(x = translateAnim.value, y = translateAnim.value),
  )
  val taskBrush = Brush.linearGradient(
    colors = shimmerTaskColors,
    start = Offset.Zero,
    end = Offset(x = translateAnim.value, y = translateAnim.value),
  )
  BandalartSkeletonScreen(
    taskBrush = taskBrush,
    subBrush = subBrush,
    mainBrush = mainBrush,
  )
}
