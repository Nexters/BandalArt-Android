@file:OptIn(ExperimentalMaterial3Api::class)

package com.nexters.bandalart.android.feature.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.bandalart.android.core.designsystem.theme.Gray100
import com.nexters.bandalart.android.core.designsystem.theme.Gray300
import com.nexters.bandalart.android.core.designsystem.theme.Gray400
import com.nexters.bandalart.android.core.designsystem.theme.Gray700
import com.nexters.bandalart.android.core.designsystem.theme.White
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.StatusBarHeightDp
import com.nexters.bandalart.android.core.ui.ThemeColor
import com.nexters.bandalart.android.core.ui.allColor
import com.nexters.bandalart.android.core.ui.component.BandalartDeleteAlertDialog
import com.nexters.bandalart.android.core.ui.component.EmojiText
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetCompleteButton
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetContentPlaceholder
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetContentText
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetDeleteButton
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetDivider
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetSubTitleText
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetTextStyle
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetTopBar
import com.nexters.bandalart.android.core.ui.extension.noRippleClickable
import com.nexters.bandalart.android.core.ui.getNavigationBarPadding
import com.nexters.bandalart.android.core.ui.nonScaleSp
import com.nexters.bandalart.android.core.util.extension.toLocalDateTime
import com.nexters.bandalart.android.core.util.extension.toStringLocalDateTime
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.extension.toLocalDateTime
import com.nexters.bandalart.android.core.ui.extension.toStringLocalDateTime
import com.nexters.bandalart.android.core.designsystem.theme.Gray100
import com.nexters.bandalart.android.core.designsystem.theme.Gray300
import com.nexters.bandalart.android.core.designsystem.theme.Gray400
import com.nexters.bandalart.android.core.designsystem.theme.Gray700
import com.nexters.bandalart.android.core.designsystem.theme.Transparent
import com.nexters.bandalart.android.core.designsystem.theme.White
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartMainCellModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartSubCellModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartTaskCellModel
import com.nexters.bandalart.android.feature.home.ui.bandalart.BandalartColorPicker
import com.nexters.bandalart.android.feature.home.ui.bandalart.BandalartDatePicker
import com.nexters.bandalart.android.feature.home.ui.bandalart.BandalartEmojiPicker
import java.time.LocalDateTime
import kotlinx.coroutines.launch

@Composable
fun BandalartBottomSheet(
  bandalartKey: String,
  isSubCell: Boolean,
  isMainCell: Boolean,
  isBlankCell: Boolean,
  cellData: BandalartCellUiModel,
  onResult: (
    bottomSheetState: Boolean,
    bottomSheetDataChangedState: Boolean,
  ) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: BottomSheetViewModel = hiltViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val focusRequester = remember { FocusRequester() }
  val focusManager = LocalFocusManager.current
  val scrollState = rememberScrollState()

  ModalBottomSheet(
    onDismissRequest = {
      viewModel.bottomSheetClosed()
      onResult(false, false)
    },
    modifier = modifier
      .wrapContentSize()
      .statusBarsPadding()
      .noRippleClickable { },
    sheetState = bottomSheetState,
    dragHandle = null,
  ) {
    LaunchedEffect(key1 = Unit) {
      viewModel.copyCellData(cellData = cellData)
    }

    LaunchedEffect(key1 = uiState.isCellDataCopied) {
      if (uiState.isCellDataCopied && uiState.cellData.title.isNullOrEmpty()) {
        focusRequester.requestFocus()
      }
    }

    LaunchedEffect(key1 = uiState.isCellUpdated) {
      if (uiState.isCellUpdated) {
        scope.launch {
          bottomSheetState.hide()
          viewModel.bottomSheetClosed()
          onResult(false, true)
        }
      }
    }

    LaunchedEffect(viewModel) {
      viewModel.eventFlow.collect { event ->
        when (event) {
          is BottomSheetUiEvent.ShowToast -> {
            Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
          }
        }
      }
    }

    if (uiState.isDeleteCellDialogOpened) {
      BandalartDeleteAlertDialog(
        title = if (isMainCell) {
          stringResource(R.string.delete_bandalart_maincelll_dialog_empty_title)
        } else if (isSubCell) {
          stringResource(R.string.delete_bandalart_subcell_dialog_empty_title)
        } else {
          stringResource(R.string.delete_bandalart_taskcell_dialog_title)
        },
        message = if (isMainCell) {
          stringResource(R.string.delete_bandalart_maincell_dialog_message)
        } else if (isSubCell) {
          stringResource(R.string.delete_bandalart_subcell_dialog_message)
        } else {
          stringResource(R.string.delete_bandalart_taskcell_dialog_message)
        },
        onDeleteClicked = {
          scope.launch {
            viewModel.deleteBandalartCell(
              bandalartKey = bandalartKey,
              cellKey = uiState.cellData.key,
            )
            viewModel.openDeleteCellDialog(flag = false)
            bottomSheetState.hide()
          }.invokeOnCompletion {
            if (!bottomSheetState.isVisible) {
              viewModel.bottomSheetClosed()
              onResult(false, true)
            }
          }
        },
        onCancelClicked = { viewModel.openDeleteCellDialog(flag = false) },
      )
    }

    Column(
      modifier = Modifier
        .background(White)
        .navigationBarsPadding()
        .noRippleClickable { focusManager.clearFocus() },
    ) {
      Spacer(modifier = Modifier.height(20.dp))
      BottomSheetTopBar(
        isMainCell = isMainCell,
        isSubCell = isSubCell,
        isBlankCell = isBlankCell,
        bottomSheetState = bottomSheetState,
        onResult = onResult,
        bottomSheetClosed = viewModel::bottomSheetClosed,
      )
      Box {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .verticalScroll(scrollState)
            .padding(
              start = 20.dp,
              top = 40.dp,
              end = 20.dp,
            ),
        ) {
          BottomSheetSubTitleText(text = stringResource(R.string.bottomsheet_title))
          Spacer(modifier = Modifier.height(11.dp))
          Row(modifier = Modifier.fillMaxWidth()) {
            if (isMainCell) {
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
                        viewModel.openEmojiPicker(flag = !uiState.isEmojiPickerOpened)
                        if (uiState.isDatePickerOpened) viewModel.openDatePicker(flag = false)
                      },
                    contentAlignment = Alignment.Center,
                  ) {
                    if (uiState.cellData.profileEmoji.isNullOrEmpty()) {
                      Image(
                        painter = painterResource(com.nexters.bandalart.android.core.designsystem.R.drawable.ic_empty_emoji),
                        contentDescription = stringResource(R.string.empty_emoji_descrption),
                      )
                    } else {
                      EmojiText(
                        emojiText = uiState.cellData.profileEmoji,
                        fontSize = 22.sp.nonScaleSp,
                      )
                    }
                  }
                }
                Image(
                  painter = painterResource(com.nexters.bandalart.android.core.designsystem.R.drawable.ic_edit),
                  contentDescription = stringResource(R.string.edit_descrption),
                  modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp),
                )
              }
            }
            Column(modifier = Modifier.padding(top = 10.dp)) {
              BasicTextField(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(18.dp)
                  .focusRequester(focusRequester),
                value = uiState.cellData.title ?: "",
                onValueChange = {
                  viewModel.titleChanged(title = if (it.length > 15) uiState.cellData.title ?: "" else it)
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                  onDone = {
                    focusManager.clearFocus()
                  },
                ),
                maxLines = 1,
                textStyle = BottomSheetTextStyle(),
                decorationBox = { innerTextField ->
                  if (uiState.cellData.title.isNullOrEmpty()) {
                    BottomSheetContentPlaceholder(
                      text = stringResource(R.string.bottomsheet_title_placeholder),
                    )
                  }
                  innerTextField()
                },
              )
              Spacer(modifier = Modifier.height(10.dp))
              BottomSheetDivider()
            }
          }
          AnimatedVisibility(visible = uiState.isEmojiPickerOpened) {
            Column(
              content = BandalartEmojiPicker(
                modifier = Modifier
                  .wrapContentSize()
                  .padding(top = 4.dp)
                  .animateContentSize(
                    animationSpec = tween(
                      durationMillis = 300,
                      easing = LinearOutSlowInEasing,
                    ),
                  ),
                currentEmoji = uiState.cellData.profileEmoji,
                isBottomSheet = false,
                onResult = { currentEmojiResult, openEmojiPushResult ->
                  viewModel.emojiSelected(profileEmoji = currentEmojiResult)
                  viewModel.openEmojiPicker(flag = openEmojiPushResult)
                },
                emojiPickerState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
              ),
            )
          }
          if (isMainCell && uiState.isCellDataCopied) {
            Spacer(modifier = Modifier.height(22.dp))
            BottomSheetSubTitleText(text = stringResource(R.string.bottomsheet_color))
            BandalartColorPicker(
              initColor = ThemeColor(
                mainColor = uiState.cellData.mainColor ?: allColor[0].mainColor,
                subColor = uiState.cellData.subColor ?: allColor[0].subColor,
              ),
              onResult = {
                viewModel.colorChanged(
                  mainColor = it.mainColor,
                  subColor = it.subColor,
                )
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
                .height(18.dp)
                .clickable {
                  viewModel.openDatePicker(flag = !uiState.isDatePickerOpened)
                  if (uiState.isEmojiPickerOpened) viewModel.openEmojiPicker(flag = false)
                },
            ) {
              if (uiState.cellData.dueDate.isNullOrEmpty()) {
                BottomSheetContentPlaceholder(text = stringResource(R.string.bottomsheet_duedate_placeholder))
              } else {
                BottomSheetContentText(
                  text = uiState.cellData.dueDate!!.toStringLocalDateTime(),
                )
              }
              Icon(
                modifier = Modifier
                  .align(Alignment.CenterEnd)
                  .height(21.dp)
                  .aspectRatio(1f),
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = stringResource(R.string.arrow_forward_descrption),
                tint = Gray400,
              )
            }
            Spacer(modifier = Modifier.height(10.dp))
            BottomSheetDivider()
          }
          AnimatedVisibility(visible = uiState.isDatePickerOpened) {
            BandalartDatePicker(
              onResult = { dueDateResult, openDatePickerPushResult ->
                viewModel.dueDateChanged(dueDate = dueDateResult.toString())
                viewModel.openDatePicker(flag = openDatePickerPushResult)
              },
              datePickerScope = scope,
              datePickerState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
              currentDueDate = uiState.cellData.dueDate?.toLocalDateTime() ?: LocalDateTime.now(),
            )
          }
          Spacer(modifier = Modifier.height(28.dp))
          BottomSheetSubTitleText(text = stringResource(R.string.bottomsheet_description))
          Spacer(modifier = Modifier.height(12.dp))
          Box {
            Column {
              BasicTextField(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(18.dp),
                value = uiState.cellData.description ?: "",
                onValueChange = {
                  viewModel.descriptionChanged(
                    description = if (it.length > 15) uiState.cellData.description else it,
                  )
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                maxLines = 1,
                textStyle = BottomSheetTextStyle(),
                decorationBox = { innerTextField ->
                  if (uiState.cellData.description.isNullOrEmpty()) {
                    BottomSheetContentPlaceholder(text = stringResource(R.string.bottomsheet_description_placeholder))
                  }
                  innerTextField()
                },
              )
              Spacer(modifier = Modifier.height(10.dp))
              BottomSheetDivider()
            }
          }
          if (!isSubCell && !isMainCell) {
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
                onCheckedChange = { switchOn -> viewModel.isCompletedChanged(flag = switchOn) },
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
                modifier = Modifier.weight(1f),
                onClick = { viewModel.openDeleteCellDialog(flag = !uiState.isDeleteCellDialogOpened) },
              )
              Spacer(modifier = Modifier.width(9.dp))
            }
            BottomSheetCompleteButton(
              modifier = Modifier.weight(1f),
              isBlankCell = uiState.cellData.title?.trim()
                .isNullOrEmpty() || (uiState.cellData == uiState.cellDataForCheck),
              onClick = {
                if (isMainCell) {
                  viewModel.updateBandalartMainCell(
                    bandalartKey = bandalartKey,
                    cellKey = cellData.key,
                    updateBandalartMainCellModel = UpdateBandalartMainCellModel(
                      title = uiState.cellData.title?.trim(),
                      description = uiState.cellData.description,
                      dueDate = uiState.cellData.dueDate?.ifEmpty { null },
                      profileEmoji = uiState.cellData.profileEmoji,
                      mainColor = uiState.cellData.mainColor ?: allColor[0].mainColor,
                      subColor = uiState.cellData.subColor ?: allColor[0].subColor,
                    ),
                  )
                } else if (isSubCell) {
                  viewModel.updateBandalartSubCell(
                    bandalartKey = bandalartKey,
                    cellKey = cellData.key,
                    updateBandalartSubCellModel = UpdateBandalartSubCellModel(
                      title = uiState.cellData.title?.trim(),
                      description = uiState.cellData.description,
                      dueDate = uiState.cellData.dueDate?.ifEmpty { null },
                    ),
                  )
                } else {
                  viewModel.updateBandalartTaskCell(
                    bandalartKey = bandalartKey,
                    cellKey = cellData.key,
                    updateBandalartTaskCellModel = UpdateBandalartTaskCellModel(
                      title = uiState.cellData.title?.trim(),
                      description = uiState.cellData.description,
                      dueDate = uiState.cellData.dueDate?.ifEmpty { null },
                      isCompleted = uiState.cellData.isCompleted,
                    ),
                  )
                }
              },
            )
          }
          Spacer(modifier = Modifier.height(StatusBarHeightDp + getNavigationBarPadding()))
        }
        if (scrollState.value > 0) {
          Column(
            modifier = Modifier.background(
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
            modifier = Modifier.background(
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
