package com.nexters.bandalart.feature.home.ui.bandalart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray200
import com.nexters.bandalart.core.designsystem.theme.Gray600
import com.nexters.bandalart.core.designsystem.theme.Gray800
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.designsystem.theme.White
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.core.ui.NavigationBarHeightDp
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.getNavigationBarPadding
import com.nexters.bandalart.feature.home.viewmodel.BottomSheetUiAction
import com.nexters.bandalart.feature.home.model.BandalartDetailUiModel
import com.nexters.bandalart.feature.home.model.dummyBandalartList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandalartListBottomSheet(
    bandalartList: ImmutableList<BandalartDetailUiModel>,
    currentBandalartId: Long,
    getBandalartDetail: (Long) -> Unit,
    setRecentBandalartId: (Long) -> Unit,
    showSkeletonChanged: (Boolean) -> Unit,
    onCancelClicked: () -> Unit,
    createBandalart: () -> Unit,
    onBottomSheetUiAction: (BottomSheetUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier
            .wrapContentSize()
            .statusBarsPadding(),
        onDismissRequest = onCancelClicked,
        sheetState = bottomSheetState,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .background(White)
                .navigationBarsPadding(),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(
                    text = stringResource(R.string.bandalart_list_title),
                    color = Gray900,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W700,
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .height(21.dp)
                        .aspectRatio(1f),
                    onClick = {
                        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) onCancelClicked()
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_description),
                        tint = Gray900,
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = NavigationBarHeightDp + getNavigationBarPadding()),
            ) {
                items(
                    count = bandalartList.size,
                    key = { index -> bandalartList[index].id },
                ) { index ->
                    val bandalartItem = bandalartList[index]
                    BandalartItem(
                        bottomSheetState = bottomSheetState,
                        bandalartItem = bandalartItem,
                        currentBandalartId = currentBandalartId,
                        onClick = { key ->
                            // 앱에 진입할때 가장 최근에 확인한 표가 화면에 보여지도록
                            setRecentBandalartId(key)
                            showSkeletonChanged(true)
                            getBandalartDetail(key)
                        },
                        onCancelClicked = onCancelClicked,
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .padding(horizontal = 24.dp),
                            onClick = createBandalart,
                            colors = ButtonDefaults.buttonColors(containerColor = Gray200),
                        ) {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_description),
                                    tint = Gray600,
                                    modifier = Modifier.size(20.dp),
                                )
                                Spacer(modifier = Modifier.padding(start = 4.dp))
                                Text(
                                    text = stringResource(R.string.bandalart_list_add),
                                    color = Gray800,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun BandalartListBottomSheetPreview() {
    BandalartTheme {
        BandalartListBottomSheet(
            bandalartList = dummyBandalartList.toImmutableList(),
            currentBandalartId = 0L,
            getBandalartDetail = {},
            setRecentBandalartId = {},
            showSkeletonChanged = {},
            onCancelClicked = {},
            createBandalart = {},
            onBottomSheetUiAction = {},
        )
    }
}
