package com.nexters.bandalart.android.feature.complete.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.ui.R

@Composable
fun CompleteTopBar(
  modifier: Modifier = Modifier,
  onNavigateBack: () -> Unit,
) {
  val context = LocalContext.current
  Row(
    modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
  ) {
    IconButton(
      onClick = onNavigateBack,
      modifier = Modifier
        .width(32.dp)
        .aspectRatio(1f),
    ) {
      Icon(
        imageVector = Icons.Default.ArrowBackIos,
        contentDescription = context.getString(R.string.arrow_forward_descrption),
        tint = Gray900,
      )
    }
  }
}
