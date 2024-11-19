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
import com.nexters.bandalart.core.ui.ThemeColor
import com.nexters.bandalart.core.ui.allColor
import com.nexters.bandalart.feature.home.model.dummyBandalartChartData

@Composable
fun BandalartCellGrid(
    bandalartId: Long,
    themeColor: ThemeColor,
    subCell: SubCell,
    rows: Int,
    cols: Int,
    bottomSheetDataChanged: (Boolean) -> Unit,
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
                        bandalartId = bandalartId,
                        themeColor = themeColor,
                        isMainCell = false,
                        cellInfo = CellInfo(
                            isSubCell = isSubCell,
                            colIndex = colIndex,
                            rowIndex = rowIndex,
                            colCnt = cols,
                            rowCnt = rows,
                        ),
                        cellData = if (isSubCell) subCell.subCellData!!
                        else subCell.subCellData!!.children[taskIndex++],
                        bottomSheetDataChanged = bottomSheetDataChanged,
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun BandalartCellGridPreview() {
    val subCellList = listOf(
        SubCell(2, 3, 1, 1, dummyBandalartChartData.children[0]),
        SubCell(3, 2, 1, 0, dummyBandalartChartData.children[1]),
        SubCell(3, 2, 1, 1, dummyBandalartChartData.children[2]),
        SubCell(2, 3, 0, 1, dummyBandalartChartData.children[3]),
    )

    BandalartTheme {
        BandalartCellGrid(
            bandalartId = 0L,
            themeColor = allColor[0],
            subCell = subCellList[1],
            rows = 3,
            cols = 3,
            bottomSheetDataChanged = {},
        )
    }
}
