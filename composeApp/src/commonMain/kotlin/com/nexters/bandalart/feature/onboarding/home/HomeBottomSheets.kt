package com.nexters.bandalart.feature.onboarding.home

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartBottomSheet
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartEmojiBottomSheet
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartListBottomSheet
import com.nexters.bandalart.feature.home.viewmodel.BottomSheetState
import com.nexters.bandalart.feature.home.viewmodel.HomeUiAction
import com.nexters.bandalart.feature.home.viewmodel.HomeUiState
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun HomeBottomSheets(
    uiState: HomeUiState,
    onHomeUiAction: (HomeUiAction) -> Unit,
) {
    val context = LocalContext.current
    when (uiState.bottomSheet) {
        is BottomSheetState.Cell -> {
            uiState.bandalartData?.let { bandalart ->
                uiState.clickedCellData?.let { cell ->
                    BandalartBottomSheet(
                        bandalartId = bandalart.id,
                        cellType = uiState.clickedCellType,
                        isBlankCell = cell.title.isNullOrEmpty(),
                        cellData = cell,
                        onHomeUiAction = onHomeUiAction,
                        bottomSheetData = BottomSheetState.Cell(
                            initialCellData = uiState.bottomSheet.initialCellData,
                            cellData = uiState.bottomSheet.cellData,
                            initialBandalartData = uiState.bottomSheet.initialBandalartData,
                            bandalartData = uiState.bottomSheet.bandalartData,
                            isDatePickerOpened = uiState.bottomSheet.isDatePickerOpened,
                            isEmojiPickerOpened = uiState.bottomSheet.isEmojiPickerOpened,
                        ),
                    )
                }
            }
        }

        is BottomSheetState.Emoji -> {
            uiState.bandalartData?.let { bandalart ->
                uiState.bandalartCellData?.let { cell ->
                    BandalartEmojiBottomSheet(
                        bandalartId = bandalart.id,
                        cellId = cell.id,
                        currentEmoji = bandalart.profileEmoji,
                        onHomeUiAction = onHomeUiAction,
                    )
                }
            }
        }

        is BottomSheetState.BandalartList -> {
            uiState.bandalartData?.let { bandalart ->
                BandalartListBottomSheet(
                    bandalartList = updateBandalartListTitles(uiState.bandalartList, context).toImmutableList(),
                    currentBandalartId = bandalart.id,
                    onHomeUiAction = onHomeUiAction,
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
