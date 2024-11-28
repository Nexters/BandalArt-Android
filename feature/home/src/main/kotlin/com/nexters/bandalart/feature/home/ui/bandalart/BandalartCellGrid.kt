package com.nexters.bandalart.feature.home.ui.bandalart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import com.nexters.bandalart.feature.home.model.dummy.dummyBandalartChartData
import com.nexters.bandalart.feature.home.viewmodel.HomeUiAction
import kotlinx.collections.immutable.persistentListOf

@Composable
fun BandalartCellGrid(
    bandalartData: BandalartUiModel,
    subCell: SubCell,
    rows: Int,
    cols: Int,
    onHomeUiAction: (HomeUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        var taskIndex = 0
        repeat(rows) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                repeat(cols) { colIndex ->
                    val isSubCell = rowIndex == subCell.subCellRowIndex && colIndex == subCell.subCellColIndex
                    BandalartCell(
                        modifier = Modifier.weight(1f),
                        bandalartData = bandalartData,
                        isMainCell = false,
                        cellInfo = CellInfo(
                            isSubCell = isSubCell,
                            colIndex = colIndex,
                            rowIndex = rowIndex,
                            colCnt = cols,
                            rowCnt = rows,
                        ),
                        cellData = if (isSubCell) subCell.subCellData!! else subCell.subCellData!!.children[taskIndex++],
                        onHomeUiAction = onHomeUiAction,
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun BandalartCellGridPreview() {
    val subCellList = persistentListOf(
        SubCell(2, 3, 1, 1, dummyBandalartChartData.children[0]),
        SubCell(3, 2, 1, 0, dummyBandalartChartData.children[1]),
        SubCell(3, 2, 1, 1, dummyBandalartChartData.children[2]),
        SubCell(2, 3, 0, 1, dummyBandalartChartData.children[3]),
    )

    BandalartTheme {
        BandalartCellGrid(
            bandalartData = BandalartUiModel(
                id = 0L,
                mainColor = "#3FFFBA",
                subColor = "#111827",
            ),
            subCell = subCellList[1],
            rows = subCellList[1].rowCnt,
            cols = subCellList[1].colCnt,
            onHomeUiAction = {},
        )
    }
}
