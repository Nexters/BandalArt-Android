@file:OptIn(ExperimentalMaterial3Api::class)

package com.nexters.bandalart.android.feature.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.core.ui.component.BottomSheetCompleteButton
import com.nexters.bandalart.android.core.ui.component.BottomSheetContentText
import com.nexters.bandalart.android.core.ui.component.BottomSheetDeleteButton
import com.nexters.bandalart.android.core.ui.component.BottomSheetSpacer
import com.nexters.bandalart.android.core.ui.component.BottomSheetSubTitleText
import com.nexters.bandalart.android.core.ui.component.BottomSheetTitle
import com.nexters.bandalart.android.core.ui.theme.Gray300
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray700
import com.nexters.bandalart.android.core.ui.theme.White
import kotlinx.coroutines.CoroutineScope

@Composable
fun BottomSheetContent(
  onResult: (Boolean) -> Unit,
  scope: CoroutineScope,
  bottomSheetState: SheetState,
  isSubCell: Boolean,
): @Composable (ColumnScope.() -> Unit) {
  return {
    val scrollState = rememberScrollState()
    Column(
      modifier = Modifier
        .padding(horizontal = 20.dp)
        // 현재 하단의 버튼이 찌부되는 현상이 있어 임시로 스크롤 달아줌
        .verticalScroll(scrollState),
    ) {
      BottomSheetTitle(
        isMainCell = false,
        isSubCell = isSubCell,
        scope = scope,
        bottomSheetState = bottomSheetState,
        onResult = onResult,
      )
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 40.dp),
      ) {
        var goal by remember { mutableStateOf("") }
        BottomSheetSubTitleText(text = "목표 이름 (필수)")
        BasicTextField(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 21.dp),
          value = goal,
          onValueChange = { goal = if (it.length > 15) goal else it },
          keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
          maxLines = 1,
          decorationBox = { innerTextField ->
            if (goal.isEmpty()) BottomSheetContentText(text = "15자 이내로 입력해주세요.")
            innerTextField()
          },
        )
      }
      BottomSheetSpacer()
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 25.dp),
      ) {
        var openDatePickerBottomSheet by rememberSaveable { mutableStateOf(false) }
        val datePickerSkipPartiallyExpanded by remember { mutableStateOf(true) }
        val datePickerScope = rememberCoroutineScope()
        val datePickerState = rememberModalBottomSheetState(
          skipPartiallyExpanded = datePickerSkipPartiallyExpanded,
        )
        BottomSheetSubTitleText(text = "마감일 선택")
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { openDatePickerBottomSheet = !openDatePickerBottomSheet },
        ) {
          BottomSheetContentText(
            modifier = Modifier.padding(top = 12.dp),
            text = "마감일을 선택해주세요.",
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
          if (openDatePickerBottomSheet) {
            DatePickerUI(
              label = "Date Picker",
              onDismissRequest = { openDatePickerBottomSheet = !openDatePickerBottomSheet },
              onResult = { openDatePickerBottomSheet = it },
              datePickerScope = datePickerScope,
              datePickerState = datePickerState,
            )
          }
        }
      }
      BottomSheetSpacer()
      Column(modifier = Modifier.padding(top = 28.dp)) {
        var memo by remember { mutableStateOf("") }
        BottomSheetSubTitleText(text = "메모 (선택)")
        BasicTextField(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
          value = memo,
          onValueChange = { memo = if (it.length > 15) memo else it },
          keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
          maxLines = 1,
          decorationBox = { innerTextField ->
            if (memo.isEmpty()) BottomSheetContentText(text = "메모를 입력해주세요.")
            innerTextField()
          },
        )
      }
      BottomSheetSpacer()
      if (!isSubCell) {
        Column(modifier = Modifier.padding(top = 28.dp)) {
          var switchOn by remember { mutableStateOf(false) }
          BottomSheetSubTitleText(text = "달성 여부")
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 9.dp),
          ) {
            BottomSheetContentText(
              modifier = Modifier.align(Alignment.CenterStart),
              text = if (switchOn) "달성" else "미달성",
            )
            Switch(
              checked = switchOn,
              onCheckedChange = { _switchOn -> switchOn = _switchOn },
              colors = SwitchDefaults.colors(
                uncheckedThumbColor = White,
                uncheckedTrackColor = Gray300,
                checkedThumbColor = White,
                checkedTrackColor = Gray700,
              ),
              modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(28.dp),
            )
          }
        }
      }
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.CenterHorizontally)
          .padding(top = 32.dp),
      ) {
        BottomSheetDeleteButton(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(9.dp))
        BottomSheetCompleteButton(modifier = Modifier.weight(1f))
      }
      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}
