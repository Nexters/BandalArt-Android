package com.nexters.bandalart.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
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
import com.nexters.bandalart.core.common.extension.toFormatDate
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray100
import com.nexters.bandalart.core.designsystem.theme.Gray300
import com.nexters.bandalart.core.designsystem.theme.Gray600
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.component.BandalartDropDownMenu
import com.nexters.bandalart.core.ui.component.CompletionRatioProgressBar
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import com.nexters.bandalart.feature.home.model.CellType
import com.nexters.bandalart.feature.home.model.dummy.dummyBandalartCellData
import com.nexters.bandalart.feature.home.model.dummy.dummyBandalartData
import com.nexters.bandalart.feature.home.viewmodel.HomeUiAction
import com.nexters.bandalart.core.designsystem.R as DesignR

@Composable
fun HomeHeader(
    bandalartData: BandalartUiModel,
    isDropDownMenuOpened: Boolean,
    cellData: BandalartCellEntity,
    onAction: (HomeUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        Column {
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .width(52.dp)
                            .aspectRatio(1f)
                            .background(Gray100)
                            .clickable { onAction(HomeUiAction.ToggleEmojiBottomSheet(true)) },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (bandalartData.profileEmoji.isNullOrEmpty()) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = DesignR.drawable.ic_empty_emoji),
                                contentDescription = stringResource(R.string.empty_emoji_description),
                                tint = Color.Unspecified,
                            )
                        } else {
                            Text(
                                text = bandalartData.profileEmoji,
                                fontSize = 22.sp,
                            )
                        }
                    }
                }
                if (bandalartData.profileEmoji.isNullOrEmpty()) {
                    Icon(
                        imageVector = ImageVector.vectorResource(DesignR.drawable.ic_edit),
                        contentDescription = stringResource(R.string.edit_description),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 4.dp, y = 4.dp),
                        tint = Color.Unspecified,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                Text(
                    text = bandalartData.title ?: stringResource(com.nexters.bandalart.core.ui.R.string.home_empty_title),
                    color = if (bandalartData.title.isNullOrEmpty()) Gray300 else Gray900,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable {
                            onAction(HomeUiAction.OnBandalartCellClick(CellType.MAIN, bandalartData.title.isNullOrEmpty(), cellData))
                        },
                    letterSpacing = (-0.4).sp,
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = DesignR.drawable.ic_option),
                    contentDescription = stringResource(R.string.option_description),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { onAction(HomeUiAction.ToggleDropDownMenu(true)) },
                    tint = Color.Unspecified,
                )
                BandalartDropDownMenu(
                    toggleDropDownMenu = { flag -> onAction(HomeUiAction.ToggleDropDownMenu(flag)) },
                    isDropDownMenuOpened = isDropDownMenuOpened,
                    onSaveClick = { onAction(HomeUiAction.OnSaveClick) },
                    onDeleteClick = { onAction(HomeUiAction.OnDeleteClick) },
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(com.nexters.bandalart.core.ui.R.string.home_complete_ratio, bandalartData.completionRatio),
                color = Gray600,
                fontSize = 12.sp,
                fontWeight = FontWeight.W500,
                letterSpacing = (-0.24).sp,
            )
            if (!bandalartData.dueDate.isNullOrEmpty()) {
                VerticalDivider(
                    modifier = Modifier
                        .height(8.dp)
                        .padding(start = 6.dp),
                    thickness = 1.dp,
                    color = Gray300,
                )
                Text(
                    text = bandalartData.dueDate.toFormatDate(),
                    color = Gray600,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(start = 6.dp),
                    letterSpacing = (-0.24).sp,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (bandalartData.isCompleted) {
                Box(
                    modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(color = bandalartData.mainColor.toColor()),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(com.nexters.bandalart.core.ui.R.string.check_description),
                            tint = Gray900,
                            modifier = Modifier.size(13.dp),
                        )
                        Text(
                            text = stringResource(com.nexters.bandalart.core.ui.R.string.home_complete),
                            color = Gray900,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.W600,
                            modifier = Modifier.padding(start = 2.dp),
                            letterSpacing = (-0.2).sp,
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        CompletionRatioProgressBar(
            completionRatio = bandalartData.completionRatio,
            progressColor = bandalartData.mainColor.toColor(),
        )
        Spacer(modifier = Modifier.height(18.dp))
    }
}

@ComponentPreview
@Composable
private fun HomeHeaderPreview() {
    BandalartTheme {
        HomeHeader(
            bandalartData = dummyBandalartData,
            isDropDownMenuOpened = false,
            cellData = dummyBandalartCellData,
            onAction = {},
        )
    }
}
