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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nexters.bandalart.android.core.designsystem.theme.Gray200
import com.nexters.bandalart.android.core.designsystem.theme.Gray400
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.designsystem.theme.White
import com.nexters.bandalart.android.core.ui.ComponentPreview
import com.nexters.bandalart.android.core.ui.R

@Composable
fun BandalartDeleteAlertDialog(
  title: String,
  message: String?,
  onDeleteClicked: () -> Unit,
  onCancelClicked: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  Dialog(onDismissRequest = { onCancelClicked() }) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = White,
    ) {
      Column(
        modifier = modifier
          .fillMaxWidth()
          .padding(top = 24.dp),
      ) {
        Image(
          imageVector = ImageVector.vectorResource(
            id = com.nexters.bandalart.android.core.designsystem.R.drawable.ic_delete,
          ),
          contentDescription = context.getString(R.string.delete_descrption),
          modifier = Modifier
            .height(28.dp)
            .align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(18.dp))
        FixedSizeText(
          text = title,
          color = Gray900,
          fontSize = 20.sp,
          fontWeight = FontWeight.W700,
          modifier = Modifier.align(Alignment.CenterHorizontally),
          textAlign = TextAlign.Center,
          lineHeight = 30.sp,
          letterSpacing = (-0.4).sp,
        )
        if (message != null) {
          Spacer(modifier = Modifier.height(8.dp))
          FixedSizeText(
            text = message,
            color = Gray400,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            letterSpacing = (-0.28).sp,
          )
        }
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
            onClick = onCancelClicked,
            colors = ButtonColors(
              containerColor = Gray200,
              contentColor = Gray900,
              disabledContainerColor = Gray200,
              disabledContentColor = Gray900,
            ),
          ) {
            FixedSizeText(
              text = context.getString(R.string.delete_bandalart_cancel),
              fontSize = 16.sp,
              fontWeight = FontWeight.W600,
              color = Gray900,
            )
          }
          Spacer(modifier = Modifier.width(9.dp))
          Button(
            modifier = Modifier
              .weight(1f)
              .height(56.dp),
            onClick = onDeleteClicked,
            colors = ButtonColors(
              containerColor = Gray900,
              contentColor = White,
              disabledContainerColor = Gray900,
              disabledContentColor = White,
            ),
          ) {
            FixedSizeText(
              text = context.getString(R.string.delete_bandalart_delete),
              color = White,
              fontSize = 16.sp,
              fontWeight = FontWeight.W600,
            )
          }
        }
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}

@ComponentPreview
@Composable
fun BandalartDeleteAlertDialogPreview() {
  BandalartDeleteAlertDialog(
    title = "반다라트를 삭제하시겠어요?",
    message = "삭제한 반다라트는 다시 복구할 수 없어요.",
    onDeleteClicked = {},
    onCancelClicked = {},
  )
}
