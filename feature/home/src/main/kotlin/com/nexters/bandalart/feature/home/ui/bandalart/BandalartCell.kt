package com.nexters.bandalart.feature.home.ui.bandalart

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.core.common.extension.toColor
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray400
import com.nexters.bandalart.core.designsystem.theme.Gray500
import com.nexters.bandalart.core.designsystem.theme.Gray600
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.designsystem.theme.White
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.component.CellText
import com.nexters.bandalart.feature.home.BandalartBottomSheet
import com.nexters.bandalart.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import com.nexters.bandalart.core.designsystem.R as DesignR

data class CellInfo(
    val isSubCell: Boolean = false,
    val colIndex: Int = 2,
    val rowIndex: Int = 2,
    val colCnt: Int = 1,
    val rowCnt: Int = 1,
    // TODO 상위 셀 정보 추가 필요(서브 목표가 비어 있을 경우, 태스크 셀을 먼저 작성할 수 없도록 validation)
    val parentCell: BandalartCellUiModel? = null,
)

data class SubCell(
    val rowCnt: Int,
    val colCnt: Int,
    val subCellRowIndex: Int,
    val subCellColIndex: Int,
    val subCellData: BandalartCellUiModel?,
)

@Composable
fun BandalartCell(
    bandalartId: Long,
    bandalartData: BandalartUiModel,
    isMainCell: Boolean,
    cellData: BandalartCellUiModel,
    bottomSheetDataChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    cellInfo: CellInfo = CellInfo(),
    outerPadding: Dp = 3.dp,
    innerPadding: Dp = 2.dp,
    mainCellPadding: Dp = 1.dp,
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .padding(
                start = if (isMainCell) mainCellPadding else if (cellInfo.colIndex == 0) outerPadding else innerPadding,
                end = if (isMainCell) mainCellPadding else if (cellInfo.colIndex == cellInfo.colCnt - 1) outerPadding else innerPadding,
                top = if (isMainCell) mainCellPadding else if (cellInfo.rowIndex == 0) outerPadding else innerPadding,
                bottom = if (isMainCell) mainCellPadding else if (cellInfo.rowIndex == cellInfo.rowCnt - 1) outerPadding else innerPadding,
            )
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(getCellBackgroundColor(bandalartData, isMainCell, cellData, cellInfo))
            .clickable {
                when {
                    // 메인셀이 비어있고, 서브나 태스크셀 클릭 시
                    !isMainCell && bandalartData.title.isNullOrEmpty() -> {
                        Toast
                            .makeText(context, context.getString(R.string.please_input_main_goal), Toast.LENGTH_SHORT)
                            .show()
                    }
//                    // 태스크셀이고 상위 서브셀이 비어있을 때(테스트셀이 자신의 부모를 알고있어야 구현 가능 함)
//                    !isMainCell && !cellInfo.isSubCell && cellInfo.parentCell?.title.isNullOrEmpty() -> {
//                        Toast.makeText(context, context.getString(R.string.please_input_sub_goal), Toast.LENGTH_SHORT).show()
//                    }
                    // 그 외의 경우 바텀시트 열기
                    else -> openBottomSheet = !openBottomSheet
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        CellContent(
            isMainCell = isMainCell,
            isSubCell = cellInfo.isSubCell,
            cellData = cellData,
            bandalartData = bandalartData,
        )
    }
    if (openBottomSheet) {
        BandalartBottomSheet(
            bandalartId = bandalartId,
            isSubCell = cellInfo.isSubCell,
            isMainCell = isMainCell,
            isBlankCell = cellData.title.isNullOrEmpty(),
            cellData = cellData,
            onResult = { bottomSheetState, bottomSheetDataChangedState ->
                openBottomSheet = bottomSheetState
                bottomSheetDataChanged(bottomSheetDataChangedState)
            },
        )
    }
}

@Composable
private fun CellContent(
    isMainCell: Boolean,
    isSubCell: Boolean,
    cellData: BandalartCellUiModel,
    bandalartData: BandalartUiModel,
) {
    when {
        isMainCell -> MainCellContent(cellData, bandalartData)
        isSubCell -> SubCellContent(cellData, bandalartData)
        else -> TaskCellContent(cellData)
    }
}

@Composable
private fun MainCellContent(
    cellData: BandalartCellUiModel,
    bandalartData: BandalartUiModel,
) {
    val cellTextColor = bandalartData.subColor.toColor()

    if (cellData.title.isNullOrEmpty()) {
        EmptyMainCellContent(cellTextColor)
    } else {
        FilledCellContent(
            title = cellData.title,
            isCompleted = cellData.isCompleted,
            textColor = cellTextColor,
            fontWeight = FontWeight.W700,
        )
    }
}

@Composable
private fun SubCellContent(
    cellData: BandalartCellUiModel,
    bandalartData: BandalartUiModel,
) {
    val cellTextColor = bandalartData.mainColor.toColor()

    if (cellData.title.isNullOrEmpty()) {
        EmptySubCellContent(cellTextColor)
    } else {
        FilledCellContent(
            title = cellData.title,
            isCompleted = cellData.isCompleted,
            textColor = cellTextColor,
            fontWeight = FontWeight.W700,
        )
    }
}

@Composable
private fun TaskCellContent(cellData: BandalartCellUiModel) {
    val cellTextColor = if (cellData.isCompleted) Gray600 else Gray900

    if (cellData.title.isNullOrEmpty()) {
        EmptyTaskContent()
    } else {
        FilledCellContent(
            title = cellData.title,
            isCompleted = cellData.isCompleted,
            textColor = cellTextColor,
            fontWeight = FontWeight.W500,
        )
    }
}

@Composable
private fun EmptyMainCellContent(textColor: Color) {
    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CellText(
                cellText = stringResource(R.string.home_main_cell),
                cellTextColor = textColor,
                fontWeight = FontWeight.W700,
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_description),
                tint = textColor,
                modifier = Modifier
                    .size(20.dp)
                    .offset(y = (-4).dp),
            )
        }
    }
}

@Composable
private fun EmptySubCellContent(textColor: Color) {
    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CellText(
                cellText = stringResource(R.string.home_sub_cell),
                cellTextColor = textColor,
                fontWeight = FontWeight.W700,
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_description),
                tint = textColor,
                modifier = Modifier
                    .size(20.dp)
                    .offset(y = (-4).dp),
            )
        }
    }
}

@Composable
private fun EmptyTaskContent() {
    Icon(
        imageVector = Icons.Default.Add,
        contentDescription = stringResource(R.string.add_description),
        tint = Gray500,
        modifier = Modifier.size(20.dp),
    )
}

@Composable
private fun FilledCellContent(
    title: String,
    isCompleted: Boolean,
    textColor: Color,
    fontWeight: FontWeight,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CellText(
            cellText = title,
            cellTextColor = textColor,
            fontWeight = fontWeight,
            textAlpha = if (isCompleted) 0.6f else 1f,
        )

        if (isCompleted) {
            Icon(
                imageVector = ImageVector.vectorResource(DesignR.drawable.ic_cell_check),
                contentDescription = stringResource(R.string.complete_description),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-4).dp, y = (-4).dp),
                tint = Color.Unspecified,
            )
        }
    }
}

private fun getCellBackgroundColor(
    bandalartData: BandalartUiModel,
    isMainCell: Boolean,
    cellData: BandalartCellUiModel,
    cellInfo: CellInfo,
): Color = when {
    // 메인 목표 달성
    isMainCell && cellData.isCompleted -> bandalartData.mainColor.toColor().copy(alpha = 0.6f)
    // 메인 목표 미달성
    isMainCell -> bandalartData.mainColor.toColor()
    // 서브 목표 달성
    cellInfo.isSubCell && cellData.isCompleted -> bandalartData.subColor.toColor().copy(alpha = 0.6f)
    // 서브 목표 미달성
    cellInfo.isSubCell -> bandalartData.subColor.toColor()
    // 태스크 목표 달성
    cellData.isCompleted -> Gray400
    else -> White
}

@ComponentPreview
@Composable
private fun BandalartCellPreview() {
    BandalartTheme {
        BandalartCell(
            bandalartId = 0L,
            bandalartData = BandalartUiModel(
                id = 0L,
                mainColor = "#FF3FFFBA",
                subColor = "#FF111827",
            ),
            isMainCell = true,
            cellData = BandalartCellUiModel(
                title = "메인 목표",
                isCompleted = false,
            ),
            bottomSheetDataChanged = {},
        )
    }
}
