package com.nexters.bandalart.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray400
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.designsystem.R as DesignR

@Composable
fun NetworkErrorAlertDialog(
    title: String,
    message: String,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = {}) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = White,
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(DesignR.drawable.ic_circle_cross),
                    contentDescription = stringResource(R.string.delete_description),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    tint = Color.Unspecified,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    color = Gray900,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    letterSpacing = (-0.4).sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    color = Gray400,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    letterSpacing = (-0.28).sp,
                )
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        onClick = onConfirmClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Gray900),
                    ) {
                        Text(
                            text = stringResource(R.string.network_error_retry_message),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600,
                            color = White,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@ComponentPreview
@Composable
private fun NetworkErrorAlertDialogPreview() {
    BandalartTheme {
        NetworkErrorAlertDialog(
            title = "네트워크 문제로 표를\n불러오지 못했어요",
            message = "다시 시도해주시기 바랍니다.",
            onConfirmClick = {},
        )
    }
}
