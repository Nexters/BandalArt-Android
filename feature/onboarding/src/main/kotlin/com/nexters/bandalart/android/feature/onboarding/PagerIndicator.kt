@file:OptIn(ExperimentalFoundationApi::class)

package com.nexters.bandalart.android.feature.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PagerIndicator(
  pageCount: Int,
  pagerState: PagerState,
) {
  Box {
    Row(
      modifier = Modifier
        .height(32.dp)
        .fillMaxWidth()
        .align(Alignment.TopCenter),
      horizontalArrangement = Arrangement.Center
    ) {
      repeat(pageCount) { iteration ->
        val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
        Box(
          modifier = Modifier
            .padding(horizontal = 5.dp)
            .clip(CircleShape)
            .background(color)
            .size(8.dp)
        )
      }
    }
  }
}
