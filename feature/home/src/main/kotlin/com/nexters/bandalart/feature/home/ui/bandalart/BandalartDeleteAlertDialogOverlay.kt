package com.nexters.bandalart.feature.home.ui.bandalart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nexters.bandalart.core.designsystem.R
import com.nexters.bandalart.core.designsystem.theme.Gray200
import com.nexters.bandalart.core.designsystem.theme.Gray400
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.designsystem.theme.White
import com.slack.circuit.overlay.Overlay
import com.slack.circuit.overlay.OverlayNavigator

sealed interface DialogResult {
    data object Confirm : DialogResult
    data object Cancel : DialogResult
    data object Dismiss : DialogResult
}

@Stable
class BandalartDeleteAlertDialogOverlay(
    private val title: String,
    private val message: String,
): Overlay<DialogResult> {
    @Composable
    override fun Content(navigator: OverlayNavigator<DialogResult>) {
        Dialog(onDismissRequest = { navigator.finish(DialogResult.Dismiss) }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = White,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
                        contentDescription = stringResource(com.nexters.bandalart.core.ui.R.string.delete_description),
                        modifier = Modifier
                            .height(28.dp)
                            .align(Alignment.CenterHorizontally),
                        tint = Color.Unspecified,
                    )
                    Spacer(modifier = Modifier.height(18.dp))
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
                    if (message != null) {
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
                    }
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
                            onClick = { navigator.finish(DialogResult.Cancel) },
                            colors = ButtonColors(
                                containerColor = Gray200,
                                contentColor = Gray900,
                                disabledContainerColor = Gray200,
                                disabledContentColor = Gray900,
                            ),
                        ) {
                            Text(
                                text = stringResource(com.nexters.bandalart.core.ui.R.string.delete_bandalart_cancel),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = Gray900,
                            )
                        }
                        Spacer(modifier = Modifier.width(9.dp))
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            onClick = { navigator.finish(DialogResult.Confirm) },
                            colors = ButtonColors(
                                containerColor = Gray900,
                                contentColor = White,
                                disabledContainerColor = Gray900,
                                disabledContentColor = White,
                            ),
                        ) {
                            Text(
                                text = stringResource(com.nexters.bandalart.core.ui.R.string.delete_bandalart_delete),
                                color = White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
