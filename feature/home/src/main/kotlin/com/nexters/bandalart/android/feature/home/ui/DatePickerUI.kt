package com.nexters.bandalart.android.feature.home.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerUI(
  label: String,
  onDismissRequest: () -> Unit,
  onResult: (Boolean) -> Unit,
  datePickerScope: CoroutineScope,
  datePickerState: SheetState,
) {
  ModalBottomSheet(
    modifier = Modifier.wrapContentSize(),
    onDismissRequest = { onResult(false) },
    sheetState = datePickerState,
    dragHandle = null,
  ) {
    Card(shape = RoundedCornerShape(10.dp)) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 10.dp, horizontal = 5.dp),
      ) {
        Text(
          text = label,
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(30.dp))

        val chosenYear = remember { mutableStateOf(currentYear.toString() + "년") }
        val chosenMonth = remember { mutableStateOf(currentMonth.toString() + "월") }
        val chosenDay = remember { mutableStateOf(currentDay.toString() + "일") }

        DateSelectionSection(
          onYearChosen = { chosenYear.value = it },
          onMonthChosen = { chosenMonth.value = it },
          onDayChosen = { chosenDay.value = it },
        )
        Spacer(modifier = Modifier.height(30.dp))

        val context = LocalContext.current
        Button(
          shape = RoundedCornerShape(5.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
          onClick = {
            Toast.makeText(context, "${chosenDay.value}-${chosenMonth.value}-${chosenYear.value}", Toast.LENGTH_SHORT).show()
            datePickerScope.launch { datePickerState.hide() }.invokeOnCompletion {
              if (!datePickerState.isVisible) {
                onResult(false)
              }
            }
            onDismissRequest()
          },
        ) {
          Text(
            text = "Done",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
          )
        }
      }
    }
  }
}

@Composable
fun DateSelectionSection(
  onYearChosen: (String) -> Unit,
  onMonthChosen: (String) -> Unit,
  onDayChosen: (String) -> Unit,
) {
  Row(
    horizontalArrangement = Arrangement.SpaceAround,
    modifier = Modifier
      .fillMaxWidth()
      .height(120.dp),
  ) {
    InfiniteItemsPicker(
      items = years,
      firstIndex = Int.MAX_VALUE / 2 + (currentYear - 1967),
      onItemSelected = onYearChosen,
    )

    InfiniteItemsPicker(
      items = monthsNumber,
      firstIndex = Int.MAX_VALUE / 2 - 4 + currentMonth,
      onItemSelected = onMonthChosen,
    )

    InfiniteItemsPicker(
      items = days,
      firstIndex = Int.MAX_VALUE / 2 + (currentDay - 2),
      onItemSelected = onDayChosen,
    )
  }
}

@Composable
fun InfiniteItemsPicker(
  items: List<String>,
  firstIndex: Int,
  onItemSelected: (String) -> Unit,
) {
  val listState = rememberLazyListState(firstIndex)
  val currentValue = remember { mutableStateOf("") }

  LaunchedEffect(key1 = !listState.isScrollInProgress) {
    onItemSelected(currentValue.value)
    listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
  }

  Box(modifier = Modifier.height(106.dp)) {
    LazyColumn(
      horizontalAlignment = Alignment.CenterHorizontally,
      state = listState,
      content = {
        items(count = Int.MAX_VALUE, itemContent = {
          val index = it % items.size
          if (it == listState.firstVisibleItemIndex + 1) {
            currentValue.value = items[index]
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = items[index],
            modifier = Modifier.alpha(if (it == listState.firstVisibleItemIndex + 1) 1f else 0.3f),
            textAlign = TextAlign.Center,
          )
          Spacer(modifier = Modifier.height(6.dp))
        })
      },
    )
  }
}

val currentYear = Calendar.getInstance().get(Calendar.YEAR)
val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

val years = (1950..2050).map { it.toString() + "년" }
val monthsNumber = (1..12).map { it.toString() + "월" }
val days = (1..31).map { it.toString() + "일" }
