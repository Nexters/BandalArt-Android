@file:OptIn(ExperimentalMaterial3Api::class)

package com.nexters.bandalart.android.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.extension.NavigationBarHeightDp
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.Gray100
import com.nexters.bandalart.android.core.ui.theme.Gray200
import com.nexters.bandalart.android.core.ui.theme.Gray600
import com.nexters.bandalart.android.core.ui.theme.Gray800
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.White
import com.nexters.bandalart.android.core.ui.theme.pretendard
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel
import kotlinx.coroutines.launch

@Composable
fun BandalartListBottomSheet(
  modifier: Modifier = Modifier,
  bandalartList: List<BandalartDetailUiModel>,
  currentBandalartKey: String,
  getBandalartDetail: (String) -> Unit,
  onCancelClicked: () -> Unit,
  createBandalart: () -> Unit,
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
          text = "반다라트 목록",
          modifier = modifier.fillMaxWidth(),
          textAlign = TextAlign.Center,
          color = Gray900,
          fontSize = 16.sp.nonScaleSp,
          fontFamily = pretendard,
          fontWeight = FontWeight.W700,
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
            contentDescription = "Clear Icon",
            tint = Gray900,
          )
        }
      }
      Spacer(modifier = Modifier.height(40.dp))
      LazyColumn(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        items(
          count = bandalartList.size,
          key = { index -> bandalartList[index].key },
        ) { index ->
          val bandalartItem = bandalartList[index]
          BandalartItem(
            modifier = Modifier
              .fillMaxWidth()
              .border(
                width = 1.5.dp,
                color = Gray100,
                shape = RoundedCornerShape(12.dp),
              )
              .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp),
            bottomSheetState = bottomSheetState,
            bandalartItem = bandalartItem,
            currentBandalartKey = currentBandalartKey,
            // TODO 해당 반다라트의 키를 로컬에 저장하여 다음에 앱에 진입할때 가장 마지막에 열었던 표가 화면에 보여지도록
            onClick = getBandalartDetail,
            onCancelClicked = onCancelClicked,
          )
        }
      }
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
              contentDescription = "Add Icon",
              tint = Gray600,
              modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.padding(start = 4.dp))
            Text(
              text = "반다라트 추가",
              fontSize = 16.sp.nonScaleSp,
              fontWeight = FontWeight.W600,
              color = Gray800,
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(NavigationBarHeightDp + 32.dp))
    }
  }
}
