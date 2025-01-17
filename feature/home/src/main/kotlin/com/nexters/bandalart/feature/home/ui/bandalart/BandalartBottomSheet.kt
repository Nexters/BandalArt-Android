package com.nexters.bandalart.feature.home.ui.bandalart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.core.common.extension.getCurrentLocale
import com.nexters.bandalart.core.common.extension.noRippleClickable
import com.nexters.bandalart.core.common.extension.toLocalDateTime
import com.nexters.bandalart.core.common.extension.toStringLocalDateTime
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray100
import com.nexters.bandalart.core.designsystem.theme.Gray300
import com.nexters.bandalart.core.designsystem.theme.Gray400
import com.nexters.bandalart.core.designsystem.theme.Gray700
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.core.ui.NavigationBarHeightDp
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.ThemeColor
import com.nexters.bandalart.core.ui.getNavigationBarPadding
import com.nexters.bandalart.feature.home.HomeScreen
import com.nexters.bandalart.feature.home.HomeScreen.Event
import com.nexters.bandalart.feature.home.model.CellType
import com.nexters.bandalart.feature.home.model.dummy.dummyBandalartCellData
import com.nexters.bandalart.feature.home.model.dummy.dummyBandalartData
import java.time.LocalDateTime
import com.nexters.bandalart.core.designsystem.R as DesignR

// TODO 완료 버튼이 활성화 되지 않는 문제 해결
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandalartBottomSheet(
    bandalartId: Long,
    cellType: CellType,
    isBlankCell: Boolean,
    cellData: BandalartCellEntity,
    bottomSheetData: HomeScreen.BottomSheetState.Cell,
    eventSink: (Event) -> Unit,
) {
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val currentLocale = context.getCurrentLocale()

    val isCompleteButtonEnabled by remember {
        derivedStateOf {
            val isTitleNotEmpty = bottomSheetData.cellData.title?.trim()?.isNotEmpty() == true
            val isDataChanged = bottomSheetData.initialCellData != bottomSheetData.cellData ||
                bottomSheetData.initialBandalartData != bottomSheetData.bandalartData

            isTitleNotEmpty && isDataChanged
        }
    }

    val showTopGradient by remember(scrollState.value) {
        derivedStateOf { scrollState.value > 0 }
    }

    val showBottomGradient by remember(scrollState.value) {
        derivedStateOf { scrollState.value < scrollState.maxValue }
    }

    ModalBottomSheet(
        onDismissRequest = {
            eventSink(Event.OnDismiss)
        },
        modifier = Modifier
            .wrapContentSize()
            .statusBarsPadding(),
        sheetState = bottomSheetState,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .background(White)
                .navigationBarsPadding()
                .noRippleClickable { focusManager.clearFocus() },
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            BottomSheetTopBar(
                cellType = cellType,
                isBlankCell = isBlankCell,
                onCloseClick = {
                    eventSink(Event.OnDismiss)
                },
            )
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .verticalScroll(scrollState)
                        .padding(start = 20.dp, top = 40.dp, end = 20.dp),
                ) {
                    BottomSheetSubTitleText(text = stringResource(R.string.bottomsheet_title))
                    Spacer(modifier = Modifier.height(11.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (cellType == CellType.MAIN) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(end = 16.dp),
                            ) {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(52.dp)
                                            .aspectRatio(1f)
                                            .background(Gray100)
                                            .clickable {
                                                eventSink(Event.OnEmojiPickerClick)
                                            },
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        if (bottomSheetData.bandalartData.profileEmoji.isNullOrEmpty()) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(DesignR.drawable.ic_empty_emoji),
                                                contentDescription = stringResource(R.string.empty_emoji_description),
                                                tint = Color.Unspecified,
                                            )
                                        } else {
                                            Text(
                                                text = bottomSheetData.bandalartData.profileEmoji,
                                                fontSize = 22.sp,
                                            )
                                        }
                                    }
                                }
                                Icon(
                                    imageVector = ImageVector.vectorResource(DesignR.drawable.ic_edit),
                                    contentDescription = stringResource(R.string.edit_description),
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .offset(x = 4.dp, y = 4.dp),
                                )
                            }
                        }
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            BandalartTextField(
                                value = bottomSheetData.cellData.title ?: "",
                                onValueChange = { title ->
                                    eventSink(Event.OnCellTitleUpdate(title, currentLocale))
                                },
                                placeholder = stringResource(R.string.bottomsheet_title_placeholder),
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp,
                                color = Gray300,
                            )
                        }
                    }
                    AnimatedVisibility(visible = bottomSheetData.isEmojiPickerOpened) {
                        Column {
                            BandalartEmojiPicker(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(top = 4.dp)
                                    .animateContentSize(
                                        animationSpec = tween(
                                            durationMillis = 300,
                                            easing = LinearOutSlowInEasing,
                                        ),
                                    ),
                                currentEmoji = bottomSheetData.bandalartData.profileEmoji,
                                isBottomSheet = false,
                                onEmojiSelect = { selectedEmoji ->
                                    eventSink(Event.OnEmojiSelect(selectedEmoji))
                                },
                            )
                        }
                    }
                    if (cellType == CellType.MAIN) {
                        Spacer(modifier = Modifier.height(22.dp))
                        BottomSheetSubTitleText(text = stringResource(R.string.bottomsheet_color))
                        BandalartColorPicker(
                            initColor = ThemeColor(
                                mainColor = bottomSheetData.bandalartData.mainColor,
                                subColor = bottomSheetData.bandalartData.subColor,
                            ),
                            onColorSelect = { themeColor ->
                                eventSink(Event.OnColorSelect(themeColor.mainColor, themeColor.subColor))
                            },
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                    }
                    Spacer(modifier = Modifier.height(25.dp))
                    BottomSheetSubTitleText(text = stringResource(R.string.bottomsheet_duedate))
                    Spacer(modifier = Modifier.height(12.dp))
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .clickable {
                                    eventSink(Event.OnDatePickerClick)
                                },
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            if (bottomSheetData.cellData.dueDate.isNullOrEmpty()) {
                                BottomSheetContentPlaceholder(text = stringResource(R.string.bottomsheet_duedate_placeholder))
                            } else {
                                BottomSheetContentText(text = bottomSheetData.cellData.dueDate!!.toStringLocalDateTime())
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = stringResource(R.string.arrow_forward_description),
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .size(24.dp),
                                tint = Gray400,
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = Gray300,
                        )
                    }
                    AnimatedVisibility(visible = bottomSheetData.isDatePickerOpened) {
                        BandalartDatePicker(
                            onDueDateSelect = { dueDateResult ->
                                eventSink(Event.OnDueDateSelect(dueDateResult.toString()))
                            },
                            currentDueDate = bottomSheetData.cellData.dueDate?.toLocalDateTime() ?: LocalDateTime.now(),
                        )
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                    BottomSheetSubTitleText(text = stringResource(R.string.bottomsheet_description))
                    Spacer(modifier = Modifier.height(12.dp))
                    Box {
                        Column {
                            BandalartTextField(
                                value = bottomSheetData.cellData.description ?: "",
                                onValueChange = { description ->
                                    eventSink(Event.OnDescriptionUpdate(description))
                                },
                                placeholder = stringResource(R.string.bottomsheet_description_placeholder),
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp,
                                color = Gray300,
                            )
                        }
                    }
                    if (cellType == CellType.TASK) {
                        Spacer(modifier = Modifier.height(28.dp))
                        BottomSheetSubTitleText(text = stringResource(R.string.bottomsheet_is_completed))
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier.fillMaxWidth()) {
                            BottomSheetContentText(
                                modifier = Modifier.align(Alignment.CenterStart),
                                text = if (bottomSheetData.cellData.isCompleted) stringResource(R.string.bottomsheet_completed)
                                else stringResource(R.string.bottomsheet_in_completed),
                            )
                            Switch(
                                checked = bottomSheetData.cellData.isCompleted,
                                onCheckedChange = { isCompleted ->
                                    eventSink(Event.OnCompletionUpdate(isCompleted))
                                },
                                colors = SwitchDefaults.colors(
                                    uncheckedThumbColor = White,
                                    uncheckedTrackColor = Gray300,
                                    uncheckedBorderColor = Gray300,
                                    checkedThumbColor = White,
                                    checkedTrackColor = Gray700,
                                    checkedBorderColor = Gray700,
                                ),
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .width(52.dp)
                                    .height(28.dp),
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 8.dp)
                            .imePadding(),
                    ) {
                        if (!isBlankCell) {
                            BottomSheetDeleteButton(
                                onClick = {
                                    eventSink(Event.OnDeleteButtonClick)
                                },
                                modifier = Modifier.weight(1f),
                            )
                            Spacer(modifier = Modifier.width(9.dp))
                        }
                        BottomSheetCompleteButton(
                            isEnabled = isCompleteButtonEnabled,
                            onClick = {
                                eventSink(Event.OnCompleteButtonClick(bandalartId, cellData.id, cellType))
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Spacer(modifier = Modifier.height(NavigationBarHeightDp + getNavigationBarPadding()))
                }

                if (showTopGradient) {
                    ScrollGradientOverlay(isTop = true)
                }

                if (showBottomGradient) {
                    ScrollGradientOverlay(
                        isTop = false,
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun BandalartMainCellBottomSheetPreview() {
    BandalartTheme {
        BandalartBottomSheet(
            bandalartId = 0L,
            cellType = CellType.MAIN,
            isBlankCell = false,
            cellData = dummyBandalartCellData,
            bottomSheetData = HomeScreen.BottomSheetState.Cell(
                initialCellData = dummyBandalartCellData,
                cellData = dummyBandalartCellData,
                initialBandalartData = dummyBandalartData,
                bandalartData = dummyBandalartData,
            ),
            eventSink = {},
        )
    }
}

@ComponentPreview
@Composable
private fun BandalartSubCellBottomSheetPreview() {
    BandalartTheme {
        BandalartBottomSheet(
            bandalartId = 0L,
            cellType = CellType.SUB,
            isBlankCell = false,
            cellData = dummyBandalartCellData,
            bottomSheetData = HomeScreen.BottomSheetState.Cell(
                initialCellData = dummyBandalartCellData,
                cellData = dummyBandalartCellData,
                initialBandalartData = dummyBandalartData,
                bandalartData = dummyBandalartData,
            ),
            eventSink = {},
        )
    }
}

@ComponentPreview
@Composable
private fun BandalartTaskCellBottomSheetPreview() {
    BandalartTheme {
        BandalartBottomSheet(
            bandalartId = 0L,
            cellType = CellType.TASK,
            isBlankCell = true,
            cellData = dummyBandalartCellData,
            bottomSheetData = HomeScreen.BottomSheetState.Cell(
                initialCellData = dummyBandalartCellData,
                cellData = dummyBandalartCellData,
                initialBandalartData = dummyBandalartData,
                bandalartData = dummyBandalartData,
            ),
            eventSink = {},
        )
    }
}
