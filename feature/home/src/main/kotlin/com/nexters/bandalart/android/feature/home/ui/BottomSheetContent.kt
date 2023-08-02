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
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetCompleteButton
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetContentText
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetDeleteButton
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetDivider
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetSubTitleText
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetTextStyle
import com.nexters.bandalart.android.core.ui.component.bottomsheet.BottomSheetTopBar
import com.nexters.bandalart.android.core.ui.extension.NavigationBarHeightDp
import com.nexters.bandalart.android.core.ui.extension.StatusBarHeightDp
import com.nexters.bandalart.android.core.ui.theme.Gray100
import com.nexters.bandalart.android.core.ui.theme.Gray300
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray700
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import com.nexters.bandalart.android.core.designsystem.R
import com.nexters.bandalart.android.core.ui.component.EmojiText
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp

@Composable
fun bottomSheetContent(
  onResult: (Boolean) -> Unit,
  scope: CoroutineScope,
  bottomSheetState: SheetState,
  isSubCell: Boolean,
  isMainCell: Boolean,
): @Composable (ColumnScope.() -> Unit) {
  return {
    var openDatePickerPush by rememberSaveable { mutableStateOf(false) }
    val datePickerSkipPartiallyExpanded by remember { mutableStateOf(true) }
    val datePickerScope = rememberCoroutineScope()
    val datePickerState = rememberModalBottomSheetState(
      skipPartiallyExpanded = datePickerSkipPartiallyExpanded,
    )
    var openEmojiPush by rememberSaveable { mutableStateOf(false) }
    val emojiSkipPartiallyExpanded by remember { mutableStateOf(true) }
    val emojiPickerScope = rememberCoroutineScope()
    val emojiPickerState = rememberModalBottomSheetState(
      skipPartiallyExpanded = emojiSkipPartiallyExpanded,
    )
    var currentEmoji by remember { mutableStateOf("") }
    var title by rememberSaveable { mutableStateOf("") }
    var dueDate by rememberSaveable { mutableStateOf("") }
    var memo by rememberSaveable { mutableStateOf("") }
    val scrollable = rememberScrollState()
    var switchOn by remember { mutableStateOf(false) }

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
                    .clickable { openEmojiPush = !openEmojiPush },
                  contentAlignment = Alignment.Center,
                ) {
                  if (currentEmoji == "") {
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
              if (currentEmoji == "") {
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
        AnimatedVisibility(visible = openEmojiPush) {
          Column(
            content = emojiPickerUI(
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
                openEmojiPush = openEmojiPushResult
              },
              emojiPickerScope = emojiPickerScope,
              emojiPickerState = emojiPickerState,
            ),
          )
        }
        Spacer(modifier = Modifier.height(25.dp))
        BottomSheetSubTitleText(text = "마감일 (선택)")
        Spacer(modifier = Modifier.height(12.dp))
        Column {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(18.dp)
              .clickable { openDatePickerPush = !openDatePickerPush },
          ) {
            val dueDateText = dueDate.split("-")
            BottomSheetContentText(
              color = if (dueDate == "") Gray400 else Gray900,
              text =
              if (dueDate == "") "마감일을 선택해주세요."
              else dueDateText[0] + "년 " + dueDateText[1] + "월 " + dueDateText[2] + "일",
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
          DatePickerUI(
            onResult = { dueDateResult, openDatePickerPushResult ->
              dueDate = dueDateResult
              openDatePickerPush = openDatePickerPushResult
            },
            datePickerScope = datePickerScope,
            datePickerState = datePickerState,
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
              value = memo,
              onValueChange = { memo = if (it.length > 15) memo else it },
              keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
              maxLines = 1,
              textStyle = BottomSheetTextStyle(),
              decorationBox = { innerTextField ->
                if (memo.isEmpty()) BottomSheetContentText(text = "메모를 입력해주세요.")
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
              text = if (switchOn) "달성" else "미달성",
              color = Gray900,
            )
            Switch(
              checked = switchOn,
              onCheckedChange = { _switchOn -> switchOn = _switchOn },
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
          BottomSheetDeleteButton(modifier = Modifier.weight(1f))
          Spacer(modifier = Modifier.width(9.dp))
          BottomSheetCompleteButton(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(StatusBarHeightDp + NavigationBarHeightDp + 20.dp))
      }
    }
  }
}
