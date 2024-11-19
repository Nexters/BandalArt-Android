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
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.core.ui.ThemeColor
import com.nexters.bandalart.core.ui.allColor
import com.nexters.bandalart.feature.home.mapper.toUiModel

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

@ComponentPreview
@Composable
private fun BandalartCellGridPreview() {
    BandalartTheme {
        val subCellData = BandalartCellEntity(
            id = 1L,
            bandalartId = 0L,
            title = "서브 목표",
            description = "서브 목표 설명",
            dueDate = null,
            isCompleted = false,
            completionRatio = 0,
            profileEmoji = null,
            mainColor = "#FF3FFFBA",
            subColor = "#FF111827",
            parentId = 0L,
        )

        val taskCells = List(8) { index ->
            BandalartCellEntity(
                id = (index + 2).toLong(),  // id는 2부터 시작 (서브셀이 1)
                bandalartId = 0L,
                title = "하위 목표 ${index + 1}",
                description = "하위 목표 ${index + 1} 설명",
                dueDate = null,
                isCompleted = false,
                completionRatio = 0,
                profileEmoji = null,
                mainColor = null,
                subColor = null,
                parentId = 1L,  // 서브셀의 id를 parentId로
            )
        }

        BandalartCellGrid(
            bandalartId = 0L,
            themeColor = allColor[0],
            subCell = SubCell(
                rowCnt = 3,
                colCnt = 3,
                subCellRowIndex = 1,
                subCellColIndex = 1,
                subCellData = subCellData.toUiModel(),
                taskCells = taskCells.map { it.toUiModel() },
            ),
            rows = 3,
            cols = 3,
            bottomSheetDataChanged = {},
        )
    }
}
