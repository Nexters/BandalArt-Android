package com.nexters.bandalart.android.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nexters.bandalart.android.core.designsystem.R
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.Gray200
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.White
import com.nexters.bandalart.android.core.ui.theme.pretendard

@Composable
fun BandalartDeleteAlertDialog(
  modifier: Modifier = Modifier,
  title: String,
  message: String,
  dialogOpened: Boolean,
  onDeleteClicked: () -> Unit,
  onCancleClicked: () -> Unit,
) {
  if (dialogOpened) {
    Dialog(onDismissRequest = { onCancleClicked() }) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = White,
      ) {
        Column(
          modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        ) {
          val image = painterResource(id = R.drawable.ic_delete)
          Image(
            painter = image,
            contentDescription = "Delete Icon",
            modifier = Modifier.height(28.dp).align(Alignment.CenterHorizontally),
          )
          Spacer(modifier = Modifier.height(18.dp))
          Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = title,
            color = Gray900,
            fontSize = 20.sp.nonScaleSp,
            fontFamily = pretendard,
            fontWeight = FontWeight.W700,
            textAlign = TextAlign.Center,
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = message,
            color = Gray400,
            fontSize = 14.sp.nonScaleSp,
            fontFamily = pretendard,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center,
          )
          Spacer(modifier = Modifier.height(30.dp))
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 24.dp)
              .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Button(
              modifier = Modifier
                .weight(1f)
                .height(56.dp),
              onClick = onCancleClicked,
              colors = ButtonColors(
                containerColor = Gray200,
                contentColor = Gray900,
                disabledContainerColor = Gray200,
                disabledContentColor = Gray900,
              ),
            ) {
              Text(
                text = "취소",
                fontSize = 16.sp.nonScaleSp,
                fontFamily = pretendard,
                fontWeight = FontWeight.W600,
              )
            }
            Spacer(modifier = Modifier.width(9.dp))
            Button(
              modifier = Modifier
                .weight(1f)
                .height(56.dp),
              onClick = {
                onDeleteClicked()
                onCancleClicked()
              },
              colors = ButtonColors(
                containerColor = Gray900,
                contentColor = White,
                disabledContainerColor = Gray900,
                disabledContentColor = White,
              ),
            ) {
              Text(
                text = "삭제하기",
                fontSize = 16.sp.nonScaleSp,
                fontFamily = pretendard,
                fontWeight = FontWeight.W600,
              )
            }
          }
          Spacer(modifier = Modifier.height(20.dp))
        }
      }
    }
  }
}