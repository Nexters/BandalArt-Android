package com.nexters.bandalart.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray600
import com.nexters.bandalart.core.designsystem.theme.White
import com.nexters.bandalart.core.designsystem.theme.pretendard
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.component.AppTitle
import com.nexters.bandalart.core.designsystem.R as DesignR

@Composable
internal fun HomeTopBar(
    bandalartCount: Int,
    onListClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(White),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AppTitle(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 20.dp, top = 2.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .padding(end = 20.dp)
                    .clickable(onClick = onListClick),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (bandalartCount > 1) {
                        Image(
                            imageVector = ImageVector.vectorResource(
                                id = DesignR.drawable.ic_hamburger,
                            ),
                            contentDescription = stringResource(R.string.hamburger_description),
                        )
                        Text(
                            text = stringResource(R.string.home_list),
                            color = Gray600,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700,
                            fontFamily = pretendard,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_description),
                            tint = Gray600,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = stringResource(R.string.home_add),
                            color = Gray600,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700,
                        )
                    }
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun HomeTopBarSingleBandalartPreview() {
    BandalartTheme {
        HomeTopBar(
            bandalartCount = 1,
            onListClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun HomeTopBarMultipleBandalartPreview() {
    BandalartTheme {
        HomeTopBar(
            bandalartCount = 2,
            onListClick = {},
        )
    }
}
