package com.nexters.bandalart.android.feature.home.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.theme.Gray200
import com.nexters.bandalart.android.core.ui.theme.Gray300
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray600
import com.nexters.bandalart.android.core.ui.theme.Gray700
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.White
import com.nexters.bandalart.android.core.ui.theme.pretendard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
fun BottomSheetContent(
  onResult: (Boolean) -> Unit,
  scope: CoroutineScope,
  bottomSheetState: SheetState,
  isSubCell: Boolean,
): @Composable (ColumnScope.() -> Unit) {
  return {
//    키보드 점프 없이 닫아주기 라는데 안되서 우선 주석 처리함
//    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    Column(modifier = Modifier
      .padding(
        start = 20.dp,
        end = 20.dp,
      )
      // 현재 하단의 버튼이 찌부되는 현상이 있어 임시로 스크롤 달아줌
      .verticalScroll(scrollState)
    ) {
      Box(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = if (isSubCell) "서브 목표 수정" else "태스크 수정",
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
          textAlign = TextAlign.Center,
          color = Gray900,
          fontSize = 16.sp,
          fontFamily = pretendard,
          fontWeight = FontWeight.W700,
        )
        IconButton(
          modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(
              top = 19.dp,
              end = 2.dp
            )
            .height(21.dp)
            .aspectRatio(1f),
          onClick = {
            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
              if (!bottomSheetState.isVisible) onResult(false)
            }
          },
        ) {
          Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Localized description",
          )
        }
      }
      Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 40.dp)
      ) {
        var goalName by remember { mutableStateOf("") }
        Text(
          text = "목표 이름 (필수)",
          textAlign = TextAlign.Start,
          color = Gray600,
          fontSize = 12.sp,
          fontFamily = pretendard,
          fontWeight = FontWeight.W700,
        )
        BasicTextField(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 21.dp),
          value = goalName,
          onValueChange = { goalName = if (it.length > 15) goalName else it },
          keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
          // 키보드 점프 없이 키보드를 닫기..라는데 안됨
//          keyboardActions = KeyboardActions(
//            onDone = {
//              keyboardController?.hide()
//            }
//          ),
          maxLines = 1,
          decorationBox = { innerTextField ->
            Box {
              if (goalName.isEmpty()) {
                Text(
                  text = "15자 이내로 입력해주세요.",
                  color = Gray400,
                  fontSize = 16.sp,
                  fontFamily = pretendard,
                  fontWeight = FontWeight.W600,
                )
              }
              innerTextField()
            }
          },
        )
      }
      Spacer(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp)
        .height(1.dp)
        .background(color = Gray300)
      )
      Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 25.dp)
      ) {
        var openDatePickerBottomSheet by rememberSaveable { mutableStateOf(false) }
        val datePickerSkipPartiallyExpanded by remember { mutableStateOf(true) }
        val datePickerScope = rememberCoroutineScope()
        val datePickerState = rememberModalBottomSheetState(
          skipPartiallyExpanded = datePickerSkipPartiallyExpanded,
        )
        Text(
          text = "마감일 선택",
          textAlign = TextAlign.Start,
          color = Gray600,
          fontSize = 12.sp,
          fontFamily = pretendard,
          fontWeight = FontWeight.W700,
        )
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { openDatePickerBottomSheet = !openDatePickerBottomSheet }
        ) {
          Text(
            modifier = Modifier.padding(top = 12.dp),
            text = "마감일을 선택해주세요.",
            color = Gray400,
            fontSize = 16.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.W600,
          )
          Icon(
            modifier = Modifier
              .align(Alignment.CenterEnd)
              .height(21.dp)
              .aspectRatio(1f),
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = "Localized description",
            tint = Gray400,
          )

          if (openDatePickerBottomSheet) {
            ModalBottomSheet(
              modifier = Modifier.wrapContentSize(),
              onDismissRequest = { openDatePickerBottomSheet = !openDatePickerBottomSheet },
              sheetState = datePickerState,
              windowInsets = WindowInsets.ime,
              dragHandle = null,
            ) {
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
      }
      Spacer(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp)
        .height(1.dp)
        .background(color = Gray300)
      )
      Column(modifier = Modifier.padding(top = 28.dp)) {
        var goalName by remember { mutableStateOf("") }
        Text(
          text = "메모 (선택)",
          textAlign = TextAlign.Start,
          color = Gray600,
          fontSize = 12.sp,
          fontFamily = pretendard,
          fontWeight = FontWeight.W700,
        )
        BasicTextField(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
          value = goalName,
          onValueChange = { goalName = if (it.length > 15) goalName else it },
          keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
          maxLines = 1,
          decorationBox = { innerTextField ->
            Box(modifier = Modifier.fillMaxWidth()) {
              if (goalName.isEmpty()) {
                Text(
                  text = "메모를 입력해주세요.",
                  color = Gray400,
                  fontSize = 16.sp,
                  fontFamily = pretendard,
                  fontWeight = FontWeight.W600,
                )
              }
              innerTextField()
            }
          },
        )
      }
      Spacer(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp)
        .height(1.dp)
        .background(color = Gray300)
      )
      if (!isSubCell) {
        Column(modifier = Modifier.padding(top = 28.dp)) {
          var switchOn by remember { mutableStateOf(false) }
          Text(
            text = "달성여부",
            textAlign = TextAlign.Start,
            color = Gray600,
            fontSize = 12.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.W700,
          )
          Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 9.dp)
          ) {
            Text(
              modifier = Modifier.align(Alignment.CenterStart),
              text = if (switchOn) "달성" else "미달성",
              textAlign = TextAlign.Start,
              color = Gray900,
              fontSize = 16.sp,
              fontFamily = pretendard,
              fontWeight = FontWeight.W600,
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
          .padding(top = 32.dp)
      ) {
        val contextForToast = LocalContext.current.applicationContext
        FilledIconButton(
          onClick = { Toast.makeText(contextForToast, "삭제 버튼", Toast.LENGTH_SHORT).show() },
          colors = IconButtonColors(Gray200, Gray900, Gray200, Gray900),
          modifier = Modifier
            .weight(1f)
            .height(55.dp),
        ) {
          Text(text = "삭제",
            fontSize = 16.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.W700,
          )
        }
        Spacer(modifier = Modifier.width(9.dp))
        FilledIconButton(
          onClick = { Toast.makeText(contextForToast, "완료 버튼", Toast.LENGTH_SHORT).show() },
          colors = IconButtonColors(Gray900, White, Gray200, Gray400),
          modifier = Modifier
            .weight(1f)
            .height(55.dp),
        ) {
          Text(
            text = "완료",
            fontSize = 16.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.W700,
          )
        }
      }
      Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(20.dp)
        .background(color = Color.Blue)
      )
    }
  }
}
