@file:OptIn(ExperimentalMaterial3Api::class)
@file:SuppressLint("FrequentlyChangedStateReadInComposition")

package com.nexters.bandalart.android.feature.home.ui.bandalart

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.nonScaleSp
import com.nexters.bandalart.android.core.designsystem.theme.Gray100
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.designsystem.theme.pretendard
import com.nexters.bandalart.android.core.ui.ComponentPreview
import com.nexters.bandalart.android.core.util.extension.toLocalDateTime
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.launch

@Composable
fun BandalartDatePicker(
  onResult: (LocalDateTime?, Boolean) -> Unit,
  datePickerState: SheetState,
  currentDueDate: LocalDateTime,
  modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()

  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    val chosenYear = remember { mutableStateOf(currentDueDate.year.toString()) }
    val chosenMonth = remember { mutableStateOf(currentDueDate.monthValue.toString()) }
    val chosenDay = remember { mutableStateOf(currentDueDate.dayOfMonth.toString()) }

    Row(
      modifier = Modifier
        .align(Alignment.End)
        .padding(
          top = 14.dp,
          end = 20.dp,
          bottom = 14.dp,
        ),
    ) {
      Text(
        modifier = Modifier
          .clickable {
            scope.launch { datePickerState.hide() }
              .invokeOnCompletion {
                if (!datePickerState.isVisible) {
                  onResult(
                    LocalDateTime.now(),
                    false,
                  )
                }
              }
          },
        text = "초기화",
        color = Gray900,
        fontFamily = pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 16.sp.nonScaleSp,
      )
      Text(
        modifier = Modifier
          .padding(start = 16.dp)
          .clickable {
            scope
              .launch { datePickerState.hide() }
              .invokeOnCompletion {
                if (!datePickerState.isVisible) {
                  onResult(
                    selectedDateWithValidate(chosenYear.value, chosenMonth.value, chosenDay.value),
                    false,
                  )
                }
              }
          },
        text = stringResource(id = R.string.bottomsheet_done),
        color = Gray900,
        fontFamily = pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 16.sp.nonScaleSp,
      )
    }
    DateSelectionSection(
      onYearChosen = { chosenYear.value = it },
      onMonthChosen = { chosenMonth.value = it },
      onDayChosen = { chosenDay.value = it },
      currentYear = chosenYear.value.toInt(),
      currentMonth = chosenMonth.value.toInt(),
      currentDay = chosenDay.value.toInt(),
    )
  }
}

val years = (2000..2050).map { it }
val monthsNumber = (1..12).map { it }
val days = (1..31).map { it }

@Composable
fun DateSelectionSection(
  onYearChosen: (String) -> Unit,
  onMonthChosen: (String) -> Unit,
  onDayChosen: (String) -> Unit,
  currentYear: Int,
  currentMonth: Int,
  currentDay: Int,
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(180.dp),
    contentAlignment = Alignment.Center,
  ) {
    Card(
      shape = RoundedCornerShape(10.dp),
      modifier = Modifier
        .height(40.dp)
        .fillMaxWidth(),
      colors = CardDefaults.cardColors(contentColor = Gray100),
      content = {},
    )
    Row(
      horizontalArrangement = Arrangement.SpaceAround,
      modifier = Modifier
        .fillMaxWidth()
        .height(180.dp),
    ) {
      InfiniteItemsPicker(
        modifier = Modifier.weight(1f),
        items = years.toImmutableList(),
        isYear = true,
        isMonth = true,
        firstIndex = Int.MAX_VALUE / 2 + (currentYear - 1963),
        onItemSelected = onYearChosen,
      )
      InfiniteItemsPicker(
        modifier = Modifier.weight(1f),
        items = monthsNumber.toImmutableList(),
        isYear = false,
        isMonth = true,
        firstIndex = Int.MAX_VALUE / 2 - 6 + currentMonth,
        onItemSelected = onMonthChosen,
      )
      InfiniteItemsPicker(
        modifier = Modifier.weight(1f),
        items = days.toImmutableList(),
        isYear = false,
        isMonth = false,
        firstIndex = Int.MAX_VALUE / 2 + (currentDay - 3),
        onItemSelected = onDayChosen,
      )
    }
  }
}

@SuppressLint("StringFormatInvalid")
@Composable
fun InfiniteItemsPicker(
  modifier: Modifier = Modifier,
  items: ImmutableList<Int>,
  isYear: Boolean,
  isMonth: Boolean,
  firstIndex: Int,
  onItemSelected: (String) -> Unit,
) {
  val listState = rememberLazyListState(firstIndex)
  val currentValue = remember { mutableStateOf("") }

  LaunchedEffect(key1 = !listState.isScrollInProgress) {
    onItemSelected(currentValue.value)
    listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
  }
  Box(modifier = modifier.height(180.dp)) {
    LazyColumn(
      horizontalAlignment = Alignment.CenterHorizontally,
      state = listState,
      content = {
        items(
          count = Int.MAX_VALUE,
          itemContent = {
            val index = it % items.size
            if (it == listState.firstVisibleItemIndex + 2) {
              currentValue.value = items[index].toString()
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = if (isYear) stringResource(R.string.datepicker_year, items[index].toString())
              else if (isMonth) stringResource(R.string.datepicker_month, items[index].toString())
              else stringResource(R.string.datepicker_day, items[index].toString()),
              modifier = modifier
                .fillMaxWidth()
                .alpha(if (it == listState.firstVisibleItemIndex + 2) 1f else 0.6f),
              textAlign = TextAlign.Center,
              color = Gray900,
              fontFamily = pretendard,
              fontWeight =
              if (it == listState.firstVisibleItemIndex + 2) FontWeight.W500
              else FontWeight.W400,
              fontSize =
              if (it == listState.firstVisibleItemIndex + 1 ||
                it == listState.firstVisibleItemIndex + 2 ||
                it == listState.firstVisibleItemIndex + 3
              ) 20.sp.nonScaleSp
              else 17.sp.nonScaleSp,
            )
            Spacer(modifier = Modifier.height(6.dp))
          },
        )
      },
    )
  }
}

fun selectedDateWithValidate(year: String, month: String, day: String): LocalDateTime {
  return "$year-${month.padStart(2, '0')}-${
    if (month == "2")
      if (day == "31" || day == "30") "28"
      else day.padStart(2, '0')
    else if (month == "4" || month == "6" || month == "9" || month == "11")
      if (day == "31") "30"
      else day.padStart(2, '0')
    else day.padStart(2, '0')
  }T00:00".toLocalDateTime()
}

@ComponentPreview
@Composable
fun BandalartDatePickerPreview() {
  BandalartDatePicker(
    onResult = { _, _ -> },
    datePickerState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    currentDueDate = LocalDateTime.now(),
  )
}

@ComponentPreview
@Composable
fun DateSelectionSectionPreview() {
  DateSelectionSection(
    onYearChosen = {},
    onMonthChosen = {},
    onDayChosen = {},
    currentYear = 2023,
    currentMonth = 12,
    currentDay = 31,
  )
}

@ComponentPreview
@Composable
fun InfiniteYearItemsPickerPreview() {
  InfiniteItemsPicker(
    items = years.toImmutableList(),
    isYear = true,
    isMonth = false,
    firstIndex = Int.MAX_VALUE / 2 + (2023 - 1963),
    onItemSelected = {},
  )
}

@ComponentPreview
@Composable
fun InfiniteMonthItemsPickerPreview() {
  InfiniteItemsPicker(
    items = monthsNumber.toImmutableList(),
    isYear = false,
    isMonth = true,
    firstIndex = Int.MAX_VALUE / 2 - 6 + 12,
    onItemSelected = {},
  )
}

@ComponentPreview
@Composable
fun InfiniteDayItemsPickerPreview() {
  InfiniteItemsPicker(
    items = days.toImmutableList(),
    isYear = false,
    isMonth = false,
    firstIndex = Int.MAX_VALUE / 2 + (31 - 3),
    onItemSelected = {},
  )
}
