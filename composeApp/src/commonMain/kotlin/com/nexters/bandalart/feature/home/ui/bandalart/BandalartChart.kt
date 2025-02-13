package com.nexters.bandalart.feature.home.ui.bandalart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import bandalart.composeapp.generated.resources.Res
import bandalart.composeapp.generated.resources.home_layout_id
import bandalart.composeapp.generated.resources.home_main_id
import com.nexters.bandalart.core.common.extension.toColor
import com.nexters.bandalart.core.designsystem.theme.Gray300
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import com.nexters.bandalart.feature.home.model.CellType
import com.nexters.bandalart.feature.home.viewmodel.HomeUiAction
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

@Composable
fun BandalartChart(
    bandalartData: BandalartUiModel,
    bandalartCellData: BandalartCellEntity,
    onHomeUiAction: (HomeUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints {
        val paddedMaxWidth = remember(maxWidth) { maxWidth - (15.dp * 2) }

        val subCellList = persistentListOf(
            SubCell(2, 3, 1, 1, bandalartCellData.children[0]),
            SubCell(3, 2, 1, 0, bandalartCellData.children[1]),
            SubCell(3, 2, 1, 1, bandalartCellData.children[2]),
            SubCell(2, 3, 0, 1, bandalartCellData.children[3]),
        )

        Layout(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            content = {
                for (index in subCellList.indices) {
                    Box(
                        modifier = Modifier
                            .layoutId(stringResource(Res.string.home_layout_id, index + 1))
                            .clip(RoundedCornerShape(12.dp))
                            .background(color = Gray300),
                    ) {
                        BandalartCellGrid(
                            bandalartData = bandalartData,
                            subCell = subCellList[index],
                            rows = subCellList[index].rowCnt,
                            cols = subCellList[index].colCnt,
                            onHomeUiAction = onHomeUiAction,
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .layoutId(stringResource(Res.string.home_main_id))
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = bandalartData.mainColor.toColor()),
                ) {
                    BandalartCell(
                        cellType = CellType.MAIN,
                        bandalartData = bandalartData,
                        cellData = bandalartCellData,
                        onHomeUiAction = onHomeUiAction,
                    )
                }
            },
        ) { measurables, constraints ->
            val sub1 = measurables.first { it.layoutId == "Sub 1" }
            val sub2 = measurables.first { it.layoutId == "Sub 2" }
            val sub3 = measurables.first { it.layoutId == "Sub 3" }
            val sub4 = measurables.first { it.layoutId == "Sub 4" }
            val main = measurables.first { it.layoutId == "Main" }

            val chartWidth = paddedMaxWidth.roundToPx()
            val mainWidth = chartWidth / 5
            val padding = 1.dp.roundToPx()

            val mainConstraints = Constraints.fixed(width = mainWidth, height = mainWidth)
            val sub1Constraints = Constraints.fixed(width = mainWidth * 3 - padding, height = mainWidth * 2 - padding)
            val sub2Constraints = Constraints.fixed(width = mainWidth * 2 - padding, height = mainWidth * 3 - padding)
            val sub3Constraints = Constraints.fixed(width = mainWidth * 2 - padding, height = mainWidth * 3 - padding)
            val sub4Constraints = Constraints.fixed(width = mainWidth * 3 - padding, height = mainWidth * 2 - padding)

            val mainPlaceable = main.measure(mainConstraints)
            val sub1Placeable = sub1.measure(sub1Constraints)
            val sub2Placeable = sub2.measure(sub2Constraints)
            val sub3Placeable = sub3.measure(sub3Constraints)
            val sub4Placeable = sub4.measure(sub4Constraints)

            layout(width = chartWidth, height = chartWidth) {
                mainPlaceable.place(x = mainWidth * 2, y = mainWidth * 2)
                sub1Placeable.place(x = 0, y = 0)
                sub2Placeable.place(x = mainWidth * 3 + padding, y = 0)
                sub3Placeable.place(x = 0, y = mainWidth * 2 + padding)
                sub4Placeable.place(x = mainWidth * 2 + padding, y = mainWidth * 3 + padding)
            }
        }
    }
}

//@ComponentPreview
//@Composable
//private fun BandalartChartPreview() {
//    BandalartTheme {
//        BandalartChart(
//            bandalartCellData = dummyBandalartChartData,
//            bandalartData = BandalartUiModel(
//                id = 0L,
//                mainColor = "#3FFFBA",
//                subColor = "#111827",
//            ),
//            onHomeUiAction = {},
//        )
//    }
//}
