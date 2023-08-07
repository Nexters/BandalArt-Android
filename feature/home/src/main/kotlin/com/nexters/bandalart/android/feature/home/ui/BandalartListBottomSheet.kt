@file:OptIn(ExperimentalMaterial3Api::class)

package com.nexters.bandalart.android.feature.home.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.extension.NavigationBarHeightDp
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.Gray200
import com.nexters.bandalart.android.core.ui.theme.Gray600
import com.nexters.bandalart.android.core.ui.theme.Gray800
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.White
import com.nexters.bandalart.android.core.ui.theme.pretendard
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel

@Composable
fun BandalartListBottomSheet(
  modifier: Modifier = Modifier,
  bandalartList: List<BandalartDetailUiModel>,
  getBandalartDetail: (String) -> Unit,
  onCancelClicked: () -> Unit,
  createBandalart: () -> Unit,
) {
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
        .navigationBarsPadding()
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
          onClick = onCancelClicked,
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(
          count = bandalartList.size,
          key = { index -> index }
        ) { index ->
          val bandalartItem = bandalartList[index]
          BandalartItem(
            modifier = modifier,
            bandalartItem = bandalartItem,
            // TODO ApiCall 을 요청하고 BottomSheet 가 닫히도록, 실패하면 BottomSheet가 닫히면 안됨
            onClick = getBandalartDetail,
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
          // TODO ApiCall 을 요청하고 BottomSheet 가 닫히도록, 실패하면 BottomSheet가 닫히면 안됨
          // TODO CreateBandalart 를 요청하고, 이를 홈 화면에 뿌려줘야 함
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