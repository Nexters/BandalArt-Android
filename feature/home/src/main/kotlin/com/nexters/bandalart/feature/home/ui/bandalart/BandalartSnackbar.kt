package com.nexters.bandalart.feature.home.ui.bandalart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray700
import com.nexters.bandalart.core.ui.ComponentPreview

@Composable
internal fun BandalartSnackbar(
    message: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Box(Modifier.fillMaxSize()) {
            Text(
                text = message,
                color = Gray700,
                fontSize = 12.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier.align(Alignment.Center),
                letterSpacing = -(0.24).sp,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun BandalartSnackbarPreview() {
    BandalartTheme {
        BandalartSnackbar(message = "새로운 반다라트를 생성했어요")
    }
}
