package com.nexters.bandalart.feature.onboarding.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.home.model.CellType
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartDeleteAlertDialog
import com.nexters.bandalart.feature.home.viewmodel.DialogState
import com.nexters.bandalart.feature.home.viewmodel.HomeUiAction
import com.nexters.bandalart.feature.home.viewmodel.HomeUiState

@Composable
internal fun HomeDialogs(
    uiState: HomeUiState,
    onHomeUiAction: (HomeUiAction) -> Unit,
) {
    when (uiState.dialog) {
        is DialogState.BandalartDelete -> {
            uiState.bandalartData?.let { bandalart ->
                BandalartDeleteAlertDialog(
                    title = if (bandalart.title.isNullOrEmpty()) {
                        stringResource(R.string.delete_bandalart_dialog_empty_title)
                    } else {
                        stringResource(R.string.delete_bandalart_dialog_title, bandalart.title)
                    },
                    message = stringResource(R.string.delete_bandalart_dialog_message),
                    onDeleteClick = {
                        onHomeUiAction(HomeUiAction.OnDeleteBandalart(bandalart.id))
                    },
                    onCancelClick = {
                        onHomeUiAction(HomeUiAction.OnCancelClick)
                    },
                )
            }
        }
        is DialogState.CellDelete -> {
            uiState.clickedCellData?.let { cellData ->
                BandalartDeleteAlertDialog(
                    title = when (uiState.clickedCellType) {
                        CellType.MAIN -> stringResource(R.string.delete_bandalart_maincell_dialog_title, cellData.title ?: "")
                        CellType.SUB -> stringResource(R.string.delete_bandalart_subcell_dialog_title, cellData.title ?: "")
                        else -> stringResource(R.string.delete_bandalart_taskcell_dialog_title, cellData.title ?: "")
                    },
                    message = when (uiState.clickedCellType) {
                        CellType.MAIN -> stringResource(R.string.delete_bandalart_maincell_dialog_message)
                        CellType.SUB -> stringResource(R.string.delete_bandalart_subcell_dialog_message)
                        else -> stringResource(R.string.delete_bandalart_taskcell_dialog_message)
                    },
                    onDeleteClick = {
                        onHomeUiAction(HomeUiAction.OnDeleteCell(cellData.id))
                    },
                    onCancelClick = {
                        onHomeUiAction(HomeUiAction.OnCancelClick)
                    },
                )
            }
        }
        null -> {}
    }
}
