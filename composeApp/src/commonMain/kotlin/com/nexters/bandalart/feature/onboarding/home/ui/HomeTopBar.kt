package com.nexters.bandalart.feature.onboarding.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bandalart.composeapp.generated.resources.Res
import com.nexters.bandalart.core.designsystem.theme.Gray600
import com.nexters.bandalart.core.designsystem.theme.pretendard
import com.nexters.bandalart.core.ui.component.AppTitle
import com.nexters.bandalart.feature.home.viewmodel.HomeUiAction
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
internal fun HomeTopBar(
    bandalartCount: Int,
    onHomeUiAction: (HomeUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(White),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AppTitle(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 20.dp, top = 2.dp)
                    .clickable {
                        onHomeUiAction(HomeUiAction.OnAppTitleClick)
                    },
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .padding(end = 20.dp)
                    .clickable(
                        onClick = {
                            if (bandalartCount > 1) {
                                onHomeUiAction(HomeUiAction.OnListClick)
                            } else {
                                onHomeUiAction(HomeUiAction.OnAddClick)
                            }
                        },
                    ),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (bandalartCount > 1) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_hamburger),
                            contentDescription = stringResource(Res.string.hamburger_description),
                            tint = Color.Unspecified,
                        )
                        Text(
                            text = stringResource(Res.string.home_list),
                            color = Gray600,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700,
                            fontFamily = pretendard,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(Res.string.add_description),
                            tint = Gray600,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = stringResource(Res.string.home_add),
                            color = Gray600,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700,
                        )
                    }
                }
            }
        }
    }
}

//@ComponentPreview
//@Composable
//private fun HomeTopBarSingleBandalartPreview() {
//    BandalartTheme {
//        HomeTopBar(
//            bandalartCount = 1,
//            onHomeUiAction = {},
//        )
//    }
//}
//
//@ComponentPreview
//@Composable
//private fun HomeTopBarMultipleBandalartPreview() {
//    BandalartTheme {
//        HomeTopBar(
//            bandalartCount = 2,
//            onHomeUiAction = {},
//        )
//    }
//}
