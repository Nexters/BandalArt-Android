package com.nexters.bandalart.feature.complete.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.core.ui.R

@Composable
fun CompleteTopBar(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
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
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = context.getString(R.string.arrow_forward_descrption),
                tint = Gray900,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun CompleteTopBarPreview() {
    BandalartTheme {
        CompleteTopBar(onNavigateBack = {})
    }
}
