@file:OptIn(ExperimentalMaterial3Api::class)
@file:SuppressLint("FrequentlyChangedStateReadInComposition")

package com.nexters.bandalart.android.feature.home.ui

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
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.Gray100
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.pretendard
import java.time.format.DateTimeFormatter
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BandalartDatePicker(
  onResult: (String, Boolean) -> Unit,
  datePickerScope: CoroutineScope,
  datePickerState: SheetState,
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxWidth(),
  ) {
    val chosenYear = remember { mutableStateOf(currentYear.toString() + "년") }
    val chosenMonth = remember { mutableStateOf(currentMonth.toString() + "월") }
    val chosenDay = remember { mutableStateOf(currentDay.toString() + "일") }

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
            datePickerScope.launch { datePickerState.hide() }
              .invokeOnCompletion {
                if (!datePickerState.isVisible) {
                  onResult(
                    "",
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
            datePickerScope.launch { datePickerState.hide() }
              .invokeOnCompletion {
                if (!datePickerState.isVisible) {
                  onResult(
                    "${chosenYear.value}-${chosenMonth.value}-${chosenDay.value}"
                      .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    false,
                  )
                }
              }
          },
        text = "완료",
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
    )
  }
}

@Composable
fun DateSelectionSection(
  onYearChosen: (String) -> Unit,
  onMonthChosen: (String) -> Unit,
  onDayChosen: (String) -> Unit,
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
      colors = CardColors(
        Gray100,
        Gray100,
        Gray100,
        Gray100,
      ),
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
        items = years,
        isYear = true,
        isMonth = true,
        firstIndex = Int.MAX_VALUE / 2 + (currentYear - 1967),
        onItemSelected = onYearChosen,
      )
      InfiniteItemsPicker(
        modifier = Modifier.weight(1f),
        items = monthsNumber,
        isYear = false,
        isMonth = true,
        firstIndex = Int.MAX_VALUE / 2 - 4 + currentMonth,
        onItemSelected = onMonthChosen,
      )
      InfiniteItemsPicker(
        modifier = Modifier.weight(1f),
        items = days,
        isYear = false,
        isMonth = false,
        firstIndex = Int.MAX_VALUE / 2 + (currentDay - 2),
        onItemSelected = onDayChosen,
      )
    }
  }
}

@Composable
fun InfiniteItemsPicker(
  modifier: Modifier = Modifier,
  items: List<Int>,
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
              text = if (isYear) items[index].toString() + "년"
              else if (isMonth) items[index].toString() + "월"
              else items[index].toString() + "일",
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
                it == listState.firstVisibleItemIndex + 3) 20.sp.nonScaleSp
              else 17.sp.nonScaleSp,
            )
            Spacer(modifier = Modifier.height(6.dp))
          },
        )
      },
    )
  }
}

val currentYear = Calendar.getInstance().get(Calendar.YEAR)
val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

val years = (1950..2050).map { it }
val monthsNumber = (1..12).map { it }
val days = (1..31).map { it }