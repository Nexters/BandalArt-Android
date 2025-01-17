package com.nexters.bandalart.feature.home

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartBottomSheet
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartEmojiBottomSheet
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartListBottomSheet
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun HomeBottomSheets(
    state: HomeScreen.State,
    eventSink: (HomeScreen.Event) -> Unit,
) {
    val context = LocalContext.current

    when (state.bottomSheet) {
        is HomeScreen.BottomSheetState.Cell -> {
            state.bandalartData?.let { bandalart ->
                state.clickedCellData?.let { cell ->
                    BandalartBottomSheet(
                        bandalartId = bandalart.id,
                        cellType = state.clickedCellType,
                        isBlankCell = cell.title.isNullOrEmpty(),
                        cellData = cell,
                        eventSink = eventSink,
                        bottomSheetData = HomeScreen.BottomSheetState.Cell(
                            initialCellData = state.bottomSheet.initialCellData,
                            cellData = state.bottomSheet.cellData,
                            initialBandalartData = state.bottomSheet.initialBandalartData,
                            bandalartData = state.bottomSheet.bandalartData,
                            isDatePickerOpened = state.bottomSheet.isDatePickerOpened,
                            isEmojiPickerOpened = state.bottomSheet.isEmojiPickerOpened,
                        ),
                    )
                }
            }
        }

        is HomeScreen.BottomSheetState.Emoji -> {
            state.bandalartData?.let { bandalart ->
                state.bandalartCellData?.let { cell ->
                    BandalartEmojiBottomSheet(
                        bandalartId = bandalart.id,
                        cellId = cell.id,
                        currentEmoji = bandalart.profileEmoji,
                        eventSink = eventSink,
                    )
                }
            }
        }

        is HomeScreen.BottomSheetState.BandalartList -> {
            state.bandalartData?.let { bandalart ->
                BandalartListBottomSheet(
                    bandalartList = updateBandalartListTitles(state.bandalartList, context).toImmutableList(),
                    currentBandalartId = bandalart.id,
                    eventSink = eventSink,
                )
            }
        }

        null -> {}
    }
}

private fun updateBandalartListTitles(
    list: List<BandalartUiModel>,
    context: Context,
): List<BandalartUiModel> {
    var counter = 1
    return list.map { item ->
        if (item.title.isNullOrEmpty()) {
            val updatedTitle = context.getString(R.string.bandalart_list_empty_title, counter)
            counter += 1
            item.copy(
                title = updatedTitle,
                isGeneratedTitle = true,
            )
        } else {
            item
        }
    }
}
