package com.nexters.bandalart.android.core.ui

import androidx.compose.ui.tooling.preview.Preview

@Preview(
  name = "Portrait",
  showBackground = true,
  device = "spec:width=360dp,height=800dp,dpi=411",
)
@Preview(
  name = "Landscape",
  showBackground = true,
  device = "spec:width=800dp,height=360dp,dpi=411",
)
annotation class DevicePreview
