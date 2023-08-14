package com.nexters.bandalart.android.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.White

@Composable
fun NetworkErrorAlertDialog(
  modifier: Modifier = Modifier,
  title: String,
  message: String,
  onConfirmClick: () -> Unit,
) {
  val context = LocalContext.current
  Dialog(onDismissRequest = {}) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = White,
    ) {
      Column(
        modifier = modifier
          .fillMaxWidth()
          .padding(top = 24.dp),
      ) {
        val image = painterResource(
          id = com.nexters.bandalart.android.core.designsystem.R.drawable.ic_circle_cross,
        )
        Image(
          painter = image,
          contentDescription = context.getString(R.string.delete_descrption),
          modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(8.dp))
        FixedSizeText(
          modifier = Modifier.align(Alignment.CenterHorizontally),
          text = title,
          color = Gray900,
          fontSize = 20.sp,
          fontWeight = FontWeight.W700,
          textAlign = TextAlign.Center,
          lineHeight = 30.sp,
          letterSpacing = (-0.4).sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        FixedSizeText(
          modifier = Modifier.align(Alignment.CenterHorizontally),
          text = message,
          color = Gray400,
          fontSize = 14.sp,
          fontWeight = FontWeight.W500,
          textAlign = TextAlign.Center,
          letterSpacing = (-0.28).sp,
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
            onClick = onConfirmClick,
            colors = ButtonDefaults.buttonColors(containerColor = Gray900),
          ) {
            Text(
              text = context.getString(R.string.network_error_retry_message),
              fontSize = 16.sp.nonScaleSp,
              fontWeight = FontWeight.W600,
              color = White,
            )
          }
        }
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}
