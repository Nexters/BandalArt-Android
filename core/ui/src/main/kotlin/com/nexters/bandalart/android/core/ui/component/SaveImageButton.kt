@file:Suppress("unused")

package com.nexters.bandalart.android.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.theme.Gray400
import com.nexters.bandalart.android.core.designsystem.theme.pretendard
import com.nexters.bandalart.android.core.designsystem.R
import com.nexters.bandalart.android.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.android.core.ui.ComponentPreview

@Composable
fun SaveImageButton(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clickable(onClick = {})
            .offset(y = (-110).dp),
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.ic_gallery,
                    ),
                    contentDescription = stringResource(com.nexters.bandalart.android.core.ui.R.string.clear_descrption),
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                textDecoration = TextDecoration.Underline,
                            ),
                        ) {
                            append(stringResource(com.nexters.bandalart.android.core.ui.R.string.save_image))
                        }
                    },
                    color = Gray400,
                    fontFamily = pretendard,
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                    letterSpacing = -(0.32).sp,
                )
            }
        }
    }
}

@ComponentPreview
@Composable
fun SaveImageButtonPreview() {
    BandalartTheme {
        SaveImageButton()
    }
}
