@file:OptIn(ExperimentalMaterial3Api::class)

package com.nexters.bandalart.android.feature.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.R
import com.nexters.bandalart.android.core.ui.component.BandalartDeleteAlertDialog
import com.nexters.bandalart.android.core.ui.component.EmojiText
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetCompleteButton
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetContentText
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetDeleteButton
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetDivider
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetSubTitleText
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetTextStyle
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetTopBar
import com.nexters.bandalart.android.core.ui.extension.NavigationBarHeightDp
import com.nexters.bandalart.android.core.ui.extension.StatusBarHeightDp
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.Gray100
import com.nexters.bandalart.android.core.ui.theme.Gray300
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray700
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.White
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartMainCellModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartSubCellModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartTaskCellModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun bottomSheetContent(
  onResult: (Boolean) -> Unit,
  scope: CoroutineScope,
  bottomSheetState: SheetState,
  isSubCell: Boolean,
  isMainCell: Boolean,
  isBlankCell: Boolean,
  cellData: BandalartCellUiModel,
  bandalartKey: String,
  updateBandalartMainCell: (String, String, UpdateBandalartMainCellModel) -> Unit,
  updateBandalartSubCell: (String, String, UpdateBandalartSubCellModel) -> Unit,
  updateBandalartTaskCell: (String, String, UpdateBandalartTaskCellModel) -> Unit,
  deleteBandalartCell: (String, String) -> Unit,
): @Composable (ColumnScope.() -> Unit) {
  return {
    var openDatePickerPush by rememberSaveable { mutableStateOf(false) }
    val datePickerSkipPartiallyExpanded by remember { mutableStateOf(true) }
    val datePickerScope = rememberCoroutineScope()
    val datePickerState = rememberModalBottomSheetState(
      skipPartiallyExpanded = datePickerSkipPartiallyExpanded,
    )
    var openEmojiPickerPush by rememberSaveable { mutableStateOf(false) }
    val emojiSkipPartiallyExpanded by remember { mutableStateOf(true) }
    val emojiPickerScope = rememberCoroutineScope()
    val emojiPickerState = rememberModalBottomSheetState(
      skipPartiallyExpanded = emojiSkipPartiallyExpanded,
    )
    var openDeleteAlertDialog by rememberSaveable { mutableStateOf(false) }
    var currentEmoji by remember { mutableStateOf(cellData.profileEmoji) }
    var title by rememberSaveable { mutableStateOf(cellData.title ?: "") }
    var mainColor by rememberSaveable { mutableStateOf(cellData.mainColor ?: "#3FFFBA") }
    var subColor by rememberSaveable { mutableStateOf(cellData.subColor ?: "#111827") }
    var dueDate by rememberSaveable { mutableStateOf(cellData.dueDate ?: "") }
    var description by rememberSaveable { mutableStateOf(cellData.description ?: "") }
    var isCompleted by remember { mutableStateOf(cellData.isCompleted) }
    val scrollable = rememberScrollState()

    if (openDeleteAlertDialog) {
      BandalartDeleteAlertDialog(
        title = "해당 셀을 삭제하시겠어요?",
        message = if (isMainCell) {
          "메인 목표 삭제는 반다라트 전체가 삭제되고 \n다시 복구할 수 없어요."
        } else if (isSubCell) {
          "서브 목표 삭제는 하위 태스크와 함께 삭제되고 \n 다시 복구할 수 없어요."
        } else {
          "삭제된 내용은 다시 복구할 수 없어요."
        },
        onDeleteClicked = {
          deleteBandalartCell(bandalartKey, cellData.key)
          openDeleteAlertDialog = false
          onResult(false)
        },
        onCancelClicked = { openDeleteAlertDialog = false },
      )
    }

    Column(
      modifier = Modifier
        .background(White)
        .navigationBarsPadding()
        .verticalScroll(scrollable),
    ) {
      Spacer(modifier = Modifier.height(20.dp))
      BottomSheetTopBar(
        isMainCell = isMainCell,
        isSubCell = isSubCell,
        isBlankCell = isBlankCell,
        scope = scope,
        bottomSheetState = bottomSheetState,
        onResult = onResult,
      )
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(
            start = 20.dp,
            top = 40.dp,
            end = 20.dp,
          ),
      ) {
        BottomSheetSubTitleText(text = "목표 이름 (필수)")
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
                      openEmojiPickerPush = !openEmojiPickerPush
                      if (openDatePickerPush) openDatePickerPush = false
                    },
                  contentAlignment = Alignment.Center,
                ) {
                  if (currentEmoji.isNullOrEmpty()) {
                    val image = painterResource(id = R.drawable.ic_empty_emoji)
                    Image(
                      painter = image,
                      contentDescription = "Empty Emoji Icon",
                    )
                  } else {
                    EmojiText(
                      emojiText = currentEmoji,
                      fontSize = 22.sp.nonScaleSp,
                    )
                  }
                }
              }
              if (currentEmoji.isNullOrEmpty()) {
                val image = painterResource(id = R.drawable.ic_edit)
                Image(
                  painter = image,
                  contentDescription = "Edit Icon",
                  modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(
                      x = 4.dp,
                      y = 4.dp,
                    ),
                )
              }
            }
          }
          Column(modifier = Modifier.padding(top = 10.dp)) {
            BasicTextField(
              modifier = Modifier
                .fillMaxWidth()
                .height(18.dp),
              value = title,
              onValueChange = { title = if (it.length > 15) title else it },
              keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
              maxLines = 1,
              textStyle = BottomSheetTextStyle(),
              decorationBox = { innerTextField ->
                if (title.isEmpty()) BottomSheetContentText(text = "15자 이내로 입력해주세요.")
                innerTextField()
              },
            )
            Spacer(modifier = Modifier.height(10.dp))
            BottomSheetDivider()
          }
        }
        AnimatedVisibility(visible = openEmojiPickerPush) {
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
              currentEmoji = currentEmoji,
              isBottomSheet = false,
              onResult = { currentEmojiResult, openEmojiPushResult ->
                currentEmoji = currentEmojiResult
                openEmojiPickerPush = openEmojiPushResult
              },
              emojiPickerScope = emojiPickerScope,
              emojiPickerState = emojiPickerState,
            ),
          )
        }
        if (isMainCell) {
          Spacer(modifier = Modifier.height(22.dp))
          BottomSheetSubTitleText(text = "색상 테마")
          BandalartColorPicker(
            initColor = ThemeColor(mainColor, subColor),
            onResult = {
              mainColor = it.mainColor
              subColor = it.subColor
            },
          )
          Spacer(modifier = Modifier.height(3.dp))
        }
        Spacer(modifier = Modifier.height(25.dp))
        BottomSheetSubTitleText(text = "마감일 (선택)")
        Spacer(modifier = Modifier.height(12.dp))
        Column {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(18.dp)
              .clickable {
                openDatePickerPush = !openDatePickerPush
                if (openEmojiPickerPush) openEmojiPickerPush = false
              },
          ) {
            val dueDateText = dueDate.split("-")
            BottomSheetContentText(
              color = if (dueDate.isEmpty()) Gray400 else Gray900,
              text =
              if (dueDate.isEmpty()) "마감일을 선택해주세요."
              else {
                dueDateText[0] + "년 " + dueDateText[1] + "월 " + dueDateText[2].split("T")[0].toInt() + "일"
              },
            )
            Icon(
              modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(21.dp)
                .aspectRatio(1f),
              imageVector = Icons.Default.ArrowForwardIos,
              contentDescription = "Arrow Forward Icon",
              tint = Gray400,
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
          BottomSheetDivider()
        }
        AnimatedVisibility(visible = openDatePickerPush) {
          BandalartDatePicker(
            onResult = { dueDateResult, openDatePickerPushResult ->
              dueDate = dueDateResult.ifEmpty { "" }
              openDatePickerPush = openDatePickerPushResult
            },
            datePickerScope = datePickerScope,
            datePickerState = datePickerState,
            currentDueDate = dueDate,
          )
        }
        Spacer(modifier = Modifier.height(28.dp))
        BottomSheetSubTitleText(text = "메모 (선택)")
        Spacer(modifier = Modifier.height(12.dp))
        Box {
          Column {
            BasicTextField(
              modifier = Modifier
                .fillMaxWidth()
                .height(18.dp),
              value = description,
              onValueChange = { description = if (it.length > 15) description else it },
              keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
              maxLines = 1,
              textStyle = BottomSheetTextStyle(),
              decorationBox = { innerTextField ->
                if (description.isEmpty()) BottomSheetContentText(text = "메모를 입력해주세요.")
                innerTextField()
              },
            )
            Spacer(modifier = Modifier.height(10.dp))
            BottomSheetDivider()
          }
        }
        Spacer(modifier = Modifier.height(28.dp))
        if (!isSubCell && !isMainCell) {
          BottomSheetSubTitleText(text = "달성 여부")
          Spacer(modifier = Modifier.height(12.dp))
          Box(modifier = Modifier.fillMaxWidth()) {
            BottomSheetContentText(
              modifier = Modifier.align(Alignment.CenterStart),
              text = if (isCompleted) "달성" else "미달성",
              color = Gray900,
            )
            Switch(
              checked = isCompleted,
              onCheckedChange = { _switchOn -> isCompleted = _switchOn },
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
        }
        Spacer(modifier = Modifier.height(32.dp))
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
              onClick = {
                scope.launch {
                  openDeleteAlertDialog = !openDeleteAlertDialog
                }.invokeOnCompletion {
                  if (!bottomSheetState.isVisible) { onResult(false) }
                }
              },
            )
            Spacer(modifier = Modifier.width(9.dp))
          }
          BottomSheetCompleteButton(
            modifier = Modifier.weight(1f),
            isBlankCell = title.isEmpty(),
            onClick = {
              scope.launch {
                if (isMainCell) {
                  updateBandalartMainCell(
                    bandalartKey,
                    cellData.key,
                    UpdateBandalartMainCellModel(
                      title = title,
                      description = description,
                      dueDate = dueDate.ifEmpty { null },
                      profileEmoji = currentEmoji,
                      mainColor = mainColor,
                      subColor = subColor,
                    ),
                  )
                } else if (isSubCell) {
                  updateBandalartSubCell(
                    bandalartKey,
                    cellData.key,
                    UpdateBandalartSubCellModel(
                      title = title,
                      description = description,
                      dueDate = dueDate.ifEmpty { null },
                    ),
                  )
                } else {
                  updateBandalartTaskCell(
                    bandalartKey,
                    cellData.key,
                    UpdateBandalartTaskCellModel(
                      title = title,
                      description = description,
                      dueDate = dueDate.ifEmpty { null },
                      isCompleted = isCompleted,
                    ),
                  )
                }

                // Todo 실패 처리해줘야함
                bottomSheetState.hide()
              }.invokeOnCompletion {
                if (!bottomSheetState.isVisible) { onResult(false) }
              }
            },
          )
        }
        Spacer(modifier = Modifier.height(StatusBarHeightDp + NavigationBarHeightDp + 20.dp))
      }
    }
  }
}

data class ThemeColor(
  val mainColor: String,
  val subColor: String,
)

val allColor = listOf(
  ThemeColor("#3FFFBA", "#3FFFBA"),
  ThemeColor("#4E3FFF", "#B5AEFF"),
  ThemeColor("#3FF3FF", "#3FF3FF"),
  ThemeColor("#93FF3F", "#93FF3F"),
  ThemeColor("#FBFF3F", "#FBFF3F"),
  ThemeColor("#FFB423", "#FFB423"),
)
