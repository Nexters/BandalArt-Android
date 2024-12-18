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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
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
import com.nexters.bandalart.core.designsystem.theme.BottomSheetContent
import com.nexters.bandalart.core.designsystem.theme.Gray100
import com.nexters.bandalart.core.designsystem.theme.Gray300
import com.nexters.bandalart.core.designsystem.theme.Gray400
import com.nexters.bandalart.core.designsystem.theme.Gray700
import com.nexters.bandalart.core.designsystem.theme.White
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.ui.NavigationBarHeightDp
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.ThemeColor
import com.nexters.bandalart.core.ui.getNavigationBarPadding
import com.nexters.bandalart.feature.home.HomeScreen.Event
import com.nexters.bandalart.feature.home.model.CellType
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartColorPicker
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartDatePicker
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartDeleteAlertDialog
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartEmojiPicker
import com.nexters.bandalart.feature.home.ui.bandalart.BottomSheetCompleteButton
import com.nexters.bandalart.feature.home.ui.bandalart.BottomSheetContentPlaceholder
import com.nexters.bandalart.feature.home.ui.bandalart.BottomSheetContentText
import com.nexters.bandalart.feature.home.ui.bandalart.BottomSheetDeleteButton
import com.nexters.bandalart.feature.home.ui.bandalart.BottomSheetResult
import com.nexters.bandalart.feature.home.ui.bandalart.BottomSheetSubTitleText
import com.nexters.bandalart.feature.home.ui.bandalart.BottomSheetTopBar
import com.nexters.bandalart.feature.home.viewmodel.BottomSheetUiAction
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuitx.overlays.BottomSheetOverlay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

// TODO 이 내부에서는 CircuitContent 사용해서 Presenter 만들어서 사용하면 지금 구조와 유사하게 만들 수 있을 듯!
// Model 을 뭘로 넣어야할까
@OptIn(ExperimentalMaterial3Api::class)
class BandalartBottomSheetOverlay(
    private val title: String,
    private val cellType: CellType,
    private val isBlankCell: Boolean,
    private val bandalartId: Long,
    private val cellData: BandalartCellEntity,
) : BottomSheetOverlay<BottomSheetResult> {
    @Composable
    override fun Content(navigator: OverlayNavigator<BottomSheetResult>) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val focusManager = LocalFocusManager.current
        val scrollState = rememberScrollState()
        val currentLocale = context.getCurrentLocale()

        LaunchedEffect(key1 = Unit) {
            copyCellData(bandalartId, cellData)
        }

        LaunchedEffect(key1 = uiState.isCellUpdated) {
            if (uiState.isCellUpdated) {
                scope.launch {
                    eventSink(Event.ToggleCellBottomSheet(false))
                    toggleCellUpdate(false)
                }
            }
        }

        if (uiState.isDeleteCellDialogOpened) {
            BandalartDeleteAlertDialog(
                title = when (cellType) {
                    CellType.MAIN -> stringResource(R.string.delete_bandalart_maincell_dialog_title, uiState.cellData.title ?: "")
                    CellType.SUB -> stringResource(R.string.delete_bandalart_subcell_dialog_title, uiState.cellData.title ?: "")
                    else -> stringResource(R.string.delete_bandalart_taskcell_dialog_title, uiState.cellData.title ?: "")
                },
                message = when (cellType) {
                    CellType.MAIN -> stringResource(R.string.delete_bandalart_maincell_dialog_message)
                    CellType.SUB -> stringResource(R.string.delete_bandalart_subcell_dialog_message)
                    else -> null
                },
                onDeleteClicked = {
                    scope.launch {
                        onBottomSheetUiAction(BottomSheetUiAction.OnDeleteDialogConfirmClick(uiState.cellData.id))
                        eventSink(Event.ToggleCellBottomSheet(false))
                    }
                },
                onCancelClicked = {
                    onBottomSheetUiAction(BottomSheetUiAction.OnDeleteDialogCancelClick)
                },
            )
        }

        ModalBottomSheet(
            onDismissRequest = {
                eventSink(Event.ToggleCellBottomSheet(false))
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
                    eventSink = eventSink,
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
                                                    onBottomSheetUiAction(BottomSheetUiAction.OnEmojiPickerClick)
                                                },
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            if (uiState.bandalartData.profileEmoji.isNullOrEmpty()) {
                                                Icon(
                                                    imageVector = ImageVector.vectorResource(com.nexters.bandalart.core.designsystem.R.drawable.ic_empty_emoji),
                                                    contentDescription = stringResource(R.string.empty_emoji_description),
                                                    tint = Color.Unspecified,
                                                )
                                            } else {
                                                Text(
                                                    text = uiState.bandalartData.profileEmoji,
                                                    fontSize = 22.sp,
                                                )
                                            }
                                        }
                                    }
                                    Icon(
                                        imageVector = ImageVector.vectorResource(com.nexters.bandalart.core.designsystem.R.drawable.ic_edit),
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
                                    value = uiState.cellData.title ?: "",
                                    onValueChange = { title ->
                                        onBottomSheetUiAction(BottomSheetUiAction.OnTitleUpdate(title, currentLocale))
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
                                        if (uiState.cellData.title.isNullOrEmpty()) {
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
                        AnimatedVisibility(visible = uiState.isEmojiPickerOpened) {
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
                                    currentEmoji = uiState.bandalartData.profileEmoji,
                                    isBottomSheet = false,
                                    onEmojiSelect = { selectedEmoji ->
                                        onBottomSheetUiAction(BottomSheetUiAction.OnEmojiSelect(selectedEmoji))
                                    },
                                )
                            }
                        }
                        if (cellType == CellType.MAIN && uiState.isCellDataCopied) {
                            Spacer(modifier = Modifier.height(22.dp))
                            BottomSheetSubTitleText(text = stringResource(R.string.bottomsheet_color))
                            BandalartColorPicker(
                                initColor = ThemeColor(
                                    mainColor = uiState.bandalartData.mainColor,
                                    subColor = uiState.bandalartData.subColor,
                                ),
                                onColorSelect = {
                                    onBottomSheetUiAction(BottomSheetUiAction.OnColorSelect(it.mainColor, it.subColor))
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
                                        onBottomSheetUiAction(BottomSheetUiAction.OnDatePickerClick)
                                    },
                                contentAlignment = Alignment.CenterStart,
                            ) {
                                if (uiState.cellData.dueDate.isNullOrEmpty()) {
                                    BottomSheetContentPlaceholder(text = stringResource(R.string.bottomsheet_duedate_placeholder))
                                } else {
                                    BottomSheetContentText(text = uiState.cellData.dueDate!!.toStringLocalDateTime())
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
                        AnimatedVisibility(visible = uiState.isDatePickerOpened) {
                            BandalartDatePicker(
                                onDueDateSelect = { dueDateResult ->
                                    onBottomSheetUiAction(BottomSheetUiAction.OnDueDateSelect(dueDateResult.toString()))
                                },
                                currentDueDate = uiState.cellData.dueDate?.toLocalDateTime() ?: LocalDateTime.now(),
                            )
                        }
                        Spacer(modifier = Modifier.height(28.dp))
                        BottomSheetSubTitleText(text = stringResource(R.string.bottomsheet_description))
                        Spacer(modifier = Modifier.height(12.dp))
                        Box {
                            Column {
                                BasicTextField(
                                    value = uiState.cellData.description ?: "",
                                    onValueChange = { description ->
                                        onBottomSheetUiAction(BottomSheetUiAction.OnDescriptionUpdate(description))
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
                                        if (uiState.cellData.description.isNullOrEmpty()) {
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
                                    text = if (uiState.cellData.isCompleted) stringResource(R.string.bottomsheet_completed)
                                    else stringResource(R.string.bottomsheet_in_completed),
                                )
                                Switch(
                                    checked = uiState.cellData.isCompleted,
                                    onCheckedChange = { isCompleted ->
                                        onBottomSheetUiAction(BottomSheetUiAction.OnCompletionUpdate(isCompleted))
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
                                        onBottomSheetUiAction(BottomSheetUiAction.OnDeleteButtonClick)
                                    },
                                    modifier = Modifier.weight(1f),
                                )
                                Spacer(modifier = Modifier.width(9.dp))
                            }
                            BottomSheetCompleteButton(
                                isEnabled = (uiState.cellData.title?.trim()
                                    ?.isNotEmpty() == true) && (uiState.cellData != uiState.initialCellData || uiState.bandalartData != uiState.initialBandalartData),
                                onClick = {
                                    onBottomSheetUiAction(BottomSheetUiAction.OnCompleteButtonClick(bandalartId, cellData.id, cellType))
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
}
