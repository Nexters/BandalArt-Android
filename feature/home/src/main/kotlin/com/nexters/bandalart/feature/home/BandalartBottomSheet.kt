package com.nexters.bandalart.feature.home

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.core.common.extension.clearFocusOnKeyboardDismiss
import com.nexters.bandalart.core.common.extension.getCurrentLocale
import com.nexters.bandalart.core.common.extension.noRippleClickable
import com.nexters.bandalart.core.common.extension.toLocalDateTime
import com.nexters.bandalart.core.common.extension.toStringLocalDateTime
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.BottomSheetContent
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
import com.nexters.bandalart.feature.home.model.CellType
import com.nexters.bandalart.feature.home.model.dummy.dummyBandalartCellData
import com.nexters.bandalart.feature.home.model.dummy.dummyBandalartData
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartColorPicker
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartDatePicker
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartEmojiPicker
import com.nexters.bandalart.feature.home.ui.bandalart.BottomSheetCompleteButton
import com.nexters.bandalart.feature.home.ui.bandalart.BottomSheetContentPlaceholder
import com.nexters.bandalart.feature.home.ui.bandalart.BottomSheetContentText
import com.nexters.bandalart.feature.home.ui.bandalart.BottomSheetDeleteButton
import com.nexters.bandalart.feature.home.ui.bandalart.BottomSheetSubTitleText
import com.nexters.bandalart.feature.home.ui.bandalart.BottomSheetTopBar
import com.nexters.bandalart.feature.home.viewmodel.BottomSheetState
import com.nexters.bandalart.feature.home.viewmodel.HomeUiAction
import java.time.LocalDateTime
import com.nexters.bandalart.core.designsystem.R as DesignR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandalartBottomSheet(
    bandalartId: Long,
    cellType: CellType,
    isBlankCell: Boolean,
    cellData: BandalartCellEntity,
    bottomSheetData: BottomSheetState.Cell,
    onHomeUiAction: (HomeUiAction) -> Unit,
) {
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val currentLocale = context.getCurrentLocale()

    ModalBottomSheet(
        onDismissRequest = {
            onHomeUiAction(HomeUiAction.OnDismiss)
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
                    onHomeUiAction(HomeUiAction.OnDismiss)
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
                                                onHomeUiAction(HomeUiAction.OnEmojiPickerClick)
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
                            BasicTextField(
                                value = bottomSheetData.cellData.title ?: "",
                                onValueChange = { title ->
                                    onHomeUiAction(HomeUiAction.OnCellTitleUpdate(title, currentLocale))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp)
                                    .clearFocusOnKeyboardDismiss(),
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                maxLines = 1,
                                textStyle = BottomSheetContent,
                                decorationBox = { innerTextField ->
                                    if (bottomSheetData.cellData.title.isNullOrEmpty()) {
                                        BottomSheetContentPlaceholder(text = stringResource(R.string.bottomsheet_title_placeholder))
                                    }
                                    innerTextField()
                                },
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
                                    onHomeUiAction(HomeUiAction.OnEmojiSelect(selectedEmoji))
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
                                onHomeUiAction(HomeUiAction.OnColorSelect(themeColor.mainColor, themeColor.subColor))
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
                                    onHomeUiAction(HomeUiAction.OnDatePickerClick)
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
                                onHomeUiAction(HomeUiAction.OnDueDateSelect(dueDateResult.toString()))
                            },
                            currentDueDate = bottomSheetData.cellData.dueDate?.toLocalDateTime() ?: LocalDateTime.now(),
                        )
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                    BottomSheetSubTitleText(text = stringResource(R.string.bottomsheet_description))
                    Spacer(modifier = Modifier.height(12.dp))
                    Box {
                        Column {
                            BasicTextField(
                                value = bottomSheetData.cellData.description ?: "",
                                onValueChange = { description ->
                                    onHomeUiAction(HomeUiAction.OnDescriptionUpdate(description))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp)
                                    .clearFocusOnKeyboardDismiss(),
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                maxLines = 1,
                                textStyle = BottomSheetContent,
                                decorationBox = { innerTextField ->
                                    if (bottomSheetData.cellData.description.isNullOrEmpty()) {
                                        BottomSheetContentPlaceholder(text = stringResource(R.string.bottomsheet_description_placeholder))
                                    }
                                    innerTextField()
                                },
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
                                    onHomeUiAction(HomeUiAction.OnCompletionUpdate(isCompleted))
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
                                    onHomeUiAction(HomeUiAction.OnDeleteButtonClick)
                                },
                                modifier = Modifier.weight(1f),
                            )
                            Spacer(modifier = Modifier.width(9.dp))
                        }
                        BottomSheetCompleteButton(
                            isEnabled = (bottomSheetData.cellData.title?.trim()
                                ?.isNotEmpty() == true) && (bottomSheetData.initialCellData != bottomSheetData.cellData || bottomSheetData.initialBandalartData != bottomSheetData.bandalartData),
                            onClick = {
                                onHomeUiAction(HomeUiAction.OnCompleteButtonClick(bandalartId, cellData.id, cellType))
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Spacer(modifier = Modifier.height(NavigationBarHeightDp + getNavigationBarPadding()))
                }
                if (scrollState.value > 0) {
                    Column(
                        modifier = Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(White, Transparent),
                                ),
                                shape = RectangleShape,
                            )
                            .height(77.dp)
                            .fillMaxWidth(),
                    ) {}
                }
                if (scrollState.value < scrollState.maxValue) {
                    Column(
                        modifier = Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Transparent, White),
                                ),
                                shape = RectangleShape,
                            )
                            .height(77.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                    ) {}
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
            bottomSheetData = BottomSheetState.Cell(
                initialCellData = dummyBandalartCellData,
                cellData = dummyBandalartCellData,
                initialBandalartData = dummyBandalartData,
                bandalartData = dummyBandalartData,
            ),
            onHomeUiAction = {},
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
            bottomSheetData = BottomSheetState.Cell(
                initialCellData = dummyBandalartCellData,
                cellData = dummyBandalartCellData,
                initialBandalartData = dummyBandalartData,
                bandalartData = dummyBandalartData,
            ),
            onHomeUiAction = {},
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
            bottomSheetData = BottomSheetState.Cell(
                initialCellData = dummyBandalartCellData,
                cellData = dummyBandalartCellData,
                initialBandalartData = dummyBandalartData,
                bandalartData = dummyBandalartData,
            ),
            onHomeUiAction = {},
        )
    }
}
