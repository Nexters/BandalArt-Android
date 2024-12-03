@file:OptIn(ExperimentalMaterial3Api::class)

package com.nexters.bandalart.feature.home.ui.bandalart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.core.common.extension.toColor
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray100
import com.nexters.bandalart.core.designsystem.theme.Gray300
import com.nexters.bandalart.core.designsystem.theme.Gray400
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import com.nexters.bandalart.feature.home.model.dummy.dummyBandalartData
import kotlinx.coroutines.launch
import com.nexters.bandalart.core.designsystem.R as DesignR

@Composable
fun BandalartListItem(
    bottomSheetState: SheetState,
    bandalartItem: BandalartUiModel,
    currentBandalartId: Long,
    onClick: (Long) -> Unit,
    onCancelClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.5.dp,
                color = if (currentBandalartId != bandalartItem.id) Gray100 else Gray300,
                shape = RoundedCornerShape(12.dp),
            )
            .clickable {
                if (currentBandalartId != bandalartItem.id) {
                    onClick(bandalartItem.id)
                }
                scope.launch { bottomSheetState.hide() }
                    .invokeOnCompletion {
                        if (!bottomSheetState.isVisible) onCancelClicked()
                    }
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Box(modifier = Modifier.align(Alignment.CenterVertically)) {
            Card(shape = RoundedCornerShape(16.dp)) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .aspectRatio(1f)
                        .background(Gray100),
                    contentAlignment = Alignment.Center,
                ) {
                    if (bandalartItem.profileEmoji.isNullOrEmpty()) {
                        Icon(
                            imageVector = ImageVector.vectorResource(DesignR.drawable.ic_empty_emoji),
                            contentDescription = stringResource(R.string.empty_emoji_description),
                            tint = Color.Unspecified,
                        )
                    } else {
                        Text(
                            text = bandalartItem.profileEmoji,
                            fontSize = 22.sp,
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
        ) {
            if (bandalartItem.isCompleted) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(color = bandalartItem.mainColor.toColor()),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.check_description),
                            tint = Gray900,
                            modifier = Modifier.size(13.dp),
                        )
                        Text(
                            text = stringResource(R.string.home_complete),
                            color = Gray900,
                            fontWeight = FontWeight.W600,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(start = 2.dp),
                            letterSpacing = (-0.2).sp,
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(color = bandalartItem.mainColor.toColor()),
                ) {
                    Row(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(
                                R.string.home_complete_ratio,
                                bandalartItem.completionRatio,
                            ),
                            color = Gray900,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.W600,
                            letterSpacing = (-0.18).sp,
                        )
                    }
                }
            }
            Text(
                text = bandalartItem.title ?: "",
                color = if (bandalartItem.isGeneratedTitle) Gray300 else Gray900,
                fontSize = 16.sp,
                fontWeight = FontWeight.W700,
                letterSpacing = (-0.32).sp,
            )
        }
        if (currentBandalartId != bandalartItem.id) {
            Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = stringResource(R.string.arrow_forward_description),
                    tint = Gray400,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun BandalartItemPreview() {
    BandalartTheme {
        BandalartListItem(
            bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            bandalartItem = dummyBandalartData,
            currentBandalartId = 0L,
            onClick = {},
            onCancelClicked = {},
        )
    }
}
