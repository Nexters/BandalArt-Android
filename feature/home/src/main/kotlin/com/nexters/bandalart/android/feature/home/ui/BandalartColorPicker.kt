package com.nexters.bandalart.android.feature.home.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.core.ui.extension.toColor
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.White

@Composable
fun BandalartColorPicker(
  initColor: ThemeColor,
  onResult: (ThemeColor) -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(45.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceEvenly,
  ) {
    var initSelected by remember { mutableStateOf(initColor) }
    val allColor = listOf(
      ThemeColor("#3FFFBA", "#3FFFBA"),
      ThemeColor("#4E3FFF", "#B5AEFF"),
      ThemeColor("#3FF3FF", "#3FF3FF"),
      ThemeColor("#93FF3F", "#93FF3F"),
      ThemeColor("#FBFF3F", "#FBFF3F"),
      ThemeColor("#FFB423", "#FFB423"),
    )
    allColor.forEach {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.weight(1f),
      ) {
        if (it.mainColor == initSelected.mainColor) {
          Card(
            border = BorderStroke(width = 1.5.dp, color = Gray900),
            modifier = Modifier
              .height(45.dp)
              .aspectRatio(1f),
            shape = RoundedCornerShape(90.dp),
            colors = CardDefaults.cardColors(White),
            content = { },
          )
        }
        Card(
          modifier = Modifier
            .height(36.dp)
            .aspectRatio(1f)
            .clickable {
              initSelected = it
              onResult(initSelected)
            },
          shape = RoundedCornerShape(90.dp),
          colors = CardDefaults.cardColors(containerColor = it.mainColor.toColor()),
          content = { },
        )
      }
    }
  }
}
