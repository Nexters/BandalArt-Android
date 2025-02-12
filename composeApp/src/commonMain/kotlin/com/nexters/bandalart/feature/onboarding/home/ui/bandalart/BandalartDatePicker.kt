@file:SuppressLint("FrequentlyChangedStateReadInComposition")

package com.nexters.bandalart.feature.onboarding.home.ui.bandalart

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bandalart.composeapp.generated.resources.Res
import com.nexters.bandalart.core.common.extension.toLocalDateTime
import com.nexters.bandalart.core.designsystem.theme.Gray200
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.designsystem.theme.pretendard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource
import java.time.LocalDateTime

private val years = (2000..2050).map { it }
private val monthsNumber = (1..12).map { it }
private val days = (1..31).map { it }

@Composable
fun BandalartDatePicker(
    onDueDateSelect: (LocalDateTime?) -> Unit,
    currentDueDate: LocalDateTime,
    modifier: Modifier = Modifier,
) {
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
                text = stringResource(Res.string.bottomsheet_reset),
                color = Gray900,
                fontSize = 16.sp,
                fontWeight = FontWeight.W700,
                modifier = Modifier
                    .clickable {
                        onDueDateSelect(LocalDateTime.now())
                    },
                fontFamily = pretendard,
            )
            Text(
                text = stringResource(Res.string.bottomsheet_done),
                color = Gray900,
                fontSize = 16.sp,
                fontWeight = FontWeight.W700,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable {
                        onDueDateSelect(selectedDateWithValidate(chosenYear.value, chosenMonth.value, chosenDay.value))
                    },
                fontFamily = pretendard,
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

@Composable
fun DateSelectionSection(
    onYearChosen: (String) -> Unit,
    onMonthChosen: (String) -> Unit,
    onDayChosen: (String) -> Unit,
    currentYear: Int,
    currentMonth: Int,
    currentDay: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.Center,
    ) {
        val yearOffset = Int.MAX_VALUE / 2 - ((Int.MAX_VALUE / 2) % years.size) + years.indexOf(currentYear)
        val monthOffset = Int.MAX_VALUE / 2 - ((Int.MAX_VALUE / 2) % monthsNumber.size) + (currentMonth - 1)
        val dayOffset = Int.MAX_VALUE / 2 - ((Int.MAX_VALUE / 2) % days.size) + (currentDay - 1)

        Box(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Gray200)
                .align(Alignment.Center),
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
                firstIndex = yearOffset,
                onItemSelected = onYearChosen,
            )
            InfiniteItemsPicker(
                modifier = Modifier.weight(1f),
                items = monthsNumber.toImmutableList(),
                isYear = false,
                isMonth = true,
                firstIndex = monthOffset,
                onItemSelected = onMonthChosen,
            )
            InfiniteItemsPicker(
                modifier = Modifier.weight(1f),
                items = days.toImmutableList(),
                isYear = false,
                isMonth = false,
                firstIndex = dayOffset,
                onItemSelected = onDayChosen,
            )
        }
    }
}

@SuppressLint("StringFormatInvalid")
@Composable
fun InfiniteItemsPicker(
    items: ImmutableList<Int>,
    isYear: Boolean,
    isMonth: Boolean,
    firstIndex: Int,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState(firstIndex)
    val currentValue = remember { mutableStateOf(items[firstIndex % items.size].toString()) }

    LaunchedEffect(key1 = !listState.isScrollInProgress) {
        if (currentValue.value.isNotEmpty()) {
            onItemSelected(currentValue.value)
            listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
        }
    }

    Box(modifier = modifier.height(180.dp)) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
            contentPadding = PaddingValues(vertical = 70.dp),
            modifier = Modifier.fillMaxHeight(),
        ) {
            items(count = Int.MAX_VALUE) { index ->
                val itemIndex = index % items.size
                val visibleItemsInfo = listState.layoutInfo.visibleItemsInfo
                val middleIndex = visibleItemsInfo.size / 2
                val isMiddleItem = visibleItemsInfo.getOrNull(middleIndex)?.index == index

                if (isMiddleItem) {
                    currentValue.value = items[itemIndex].toString()
                }

                Box(
                    modifier = Modifier.height(40.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = when {
                            isYear -> stringResource(Res.string.datepicker_year, items[itemIndex].toString())
                            isMonth -> stringResource(Res.string.datepicker_month, items[itemIndex].toString())
                            else -> stringResource(Res.string.datepicker_day, items[itemIndex].toString())
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(if (isMiddleItem) 1f else 0.6f),
                        color = Gray900,
                        fontSize = if (isMiddleItem) 20.sp else 17.sp,
                        fontWeight = if (isMiddleItem) FontWeight.W500 else FontWeight.W400,
                        textAlign = TextAlign.Center,
                        fontFamily = pretendard,
                    )
                }
            }
        }
    }
}

fun selectedDateWithValidate(year: String, month: String, day: String): LocalDateTime {
    val yearNum = year.filter { it.isDigit() }
    val monthNum = month.filter { it.isDigit() }
    val dayNum = day.filter { it.isDigit() }

    return "$yearNum-${monthNum.padStart(2, '0')}-${
        if (monthNum == "2")
            if (dayNum == "31" || dayNum == "30") "28"
            else dayNum.padStart(2, '0')
        else if (monthNum in listOf("4", "6", "9", "11"))
            if (dayNum == "31") "30"
            else dayNum.padStart(2, '0')
        else dayNum.padStart(2, '0')
    }T00:00".toLocalDateTime()
}

//@ComponentPreview
//@Composable
//private fun BandalartDatePickerPreview() {
//    BandalartTheme {
//        BandalartDatePicker(
//            onDueDateSelect = {},
//            currentDueDate = LocalDateTime.now(),
//        )
//    }
//}
//
//@ComponentPreview
//@Composable
//private fun DateSelectionSectionPreview() {
//    BandalartTheme {
//        DateSelectionSection(
//            onYearChosen = {},
//            onMonthChosen = {},
//            onDayChosen = {},
//            currentYear = 2024,
//            currentMonth = 12,
//            currentDay = 31,
//        )
//    }
//}
//
//@ComponentPreview
//@Composable
//private fun InfiniteYearItemsPickerPreview() {
//    BandalartTheme {
//        InfiniteItemsPicker(
//            items = years.toImmutableList(),
//            isYear = true,
//            isMonth = false,
//            firstIndex = Int.MAX_VALUE / 2 + (2024 - years.first()),
//            onItemSelected = {},
//        )
//    }
//}
//
//@ComponentPreview
//@Composable
//private fun InfiniteMonthItemsPickerPreview() {
//    BandalartTheme {
//        InfiniteItemsPicker(
//            items = monthsNumber.toImmutableList(),
//            isYear = false,
//            isMonth = true,
//            firstIndex = Int.MAX_VALUE / 2 + (31 - 1),
//            onItemSelected = {},
//        )
//    }
//}
//
//@ComponentPreview
//@Composable
//private fun InfiniteDayItemsPickerPreview() {
//    BandalartTheme {
//        InfiniteItemsPicker(
//            items = days.toImmutableList(),
//            isYear = false,
//            isMonth = false,
//            firstIndex = Int.MAX_VALUE / 2 + (31 - 3),
//            onItemSelected = {},
//        )
//    }
//}
