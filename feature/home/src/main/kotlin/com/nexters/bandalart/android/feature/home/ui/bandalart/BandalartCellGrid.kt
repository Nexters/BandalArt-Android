package com.nexters.bandalart.android.feature.home.ui.bandalart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nexters.bandalart.android.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.android.core.ui.ComponentPreview
import com.nexters.bandalart.android.core.ui.ThemeColor
import com.nexters.bandalart.android.core.ui.allColor
import com.nexters.bandalart.android.feature.home.model.dummyBandalartChartData

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
            else subCell.taskCells[taskIndex++],
            bottomSheetDataChanged = bottomSheetDataChanged,
          )
        }
      }
    }
  }
}
