package com.nexters.bandalart.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.home.HomeScreen.Event
import com.nexters.bandalart.feature.home.model.CellType
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartDeleteAlertDialog

@Composable
internal fun HomeDialogs(
    state: HomeScreen.State,
    eventSink: (Event) -> Unit
) {
    when (state.dialog) {
        is HomeScreen.DialogState.BandalartDelete -> {
            state.bandalartData?.let { bandalart ->
                BandalartDeleteAlertDialog(
                    title = if (bandalart.title.isNullOrEmpty()) {
                        stringResource(R.string.delete_bandalart_dialog_empty_title)
                    } else {
                        stringResource(R.string.delete_bandalart_dialog_title, bandalart.title)
                    },
                    message = stringResource(R.string.delete_bandalart_dialog_message),
                    onDeleteClick = {
                        eventSink(Event.OnDeleteBandalart(bandalart.id))
                    },
                    onCancelClick = {
                        eventSink(Event.OnCancelClick)
                    },
                )
            }
        }

        is HomeScreen.DialogState.CellDelete -> {
            state.clickedCellData?.let { cellData ->
                BandalartDeleteAlertDialog(
                    title = when (state.clickedCellType) {
                        CellType.MAIN -> stringResource(R.string.delete_bandalart_maincell_dialog_title, cellData.title ?: "")
                        CellType.SUB -> stringResource(R.string.delete_bandalart_subcell_dialog_title, cellData.title ?: "")
                        else -> stringResource(R.string.delete_bandalart_taskcell_dialog_title, cellData.title ?: "")
                    },
                    message = when (state.clickedCellType) {
                        CellType.MAIN -> stringResource(R.string.delete_bandalart_maincell_dialog_message)
                        CellType.SUB -> stringResource(R.string.delete_bandalart_subcell_dialog_message)
                        else -> stringResource(R.string.delete_bandalart_taskcell_dialog_message)
                    },
                    onDeleteClick = {
                        eventSink(Event.OnDeleteCell(cellData.id))
                    },
                    onCancelClick = {
                        eventSink(Event.OnCancelClick)
                    },
                )
            }
        }

        null -> {}
    }
}
