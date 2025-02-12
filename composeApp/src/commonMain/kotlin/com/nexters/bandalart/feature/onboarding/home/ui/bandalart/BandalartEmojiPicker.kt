package com.nexters.bandalart.feature.onboarding.home.ui.bandalart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.core.designsystem.theme.Gray100
import com.nexters.bandalart.core.designsystem.theme.Gray400
import com.nexters.bandalart.core.ui.NavigationBarHeightDp
import kotlinx.collections.immutable.persistentListOf

private val emojiList = persistentListOf(
    "🔥", "😀", "😃", "😄", "😆", "🥹",
    "🥰", "😍", "😂", "🥲", "☺️", "😎",
    "🥳", "🤩", "⭐", "🌟", "✨", "💥",
    "❤️", "🧡", "💛", "💚", "💙", "❤️‍🔥",
)

@Composable
fun BandalartEmojiPicker(
    currentEmoji: String?,
    isBottomSheet: Boolean,
    onEmojiSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedEmoji by remember { mutableStateOf(currentEmoji) }
    var prevSelectedEmoji by remember { mutableStateOf(currentEmoji) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .padding(
                top = if (isBottomSheet) 16.dp else 0.dp,
            ),
    ) {
        var emojiIndex = 0
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = if (isBottomSheet) 15.dp else 0.dp,
                    start = if (isBottomSheet) 15.dp else 0.dp,
                    end = if (isBottomSheet) 23.dp else 8.dp,
                    bottom = if (isBottomSheet) 26.dp else 0.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            repeat(4) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    repeat(6) {
                        val emojiItem = emojiList[emojiIndex++]
                        Card(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            border = when (emojiItem) {
                                selectedEmoji -> {
                                    BorderStroke(
                                        width = 1.dp,
                                        color = Gray400,
                                    )
                                }

                                prevSelectedEmoji -> {
                                    BorderStroke(
                                        width = 1.dp,
                                        color = Color.Transparent,
                                    )
                                }

                                else -> null
                            },
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .background(color = Gray100)
                                    .clickable {
                                        if (selectedEmoji == emojiItem) {
                                            return@clickable
                                        } else {
                                            prevSelectedEmoji = selectedEmoji
                                            selectedEmoji = emojiItem
                                            onEmojiSelect(emojiItem)
                                        }
                                    },
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = emojiItem,
                                    fontSize = 24.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(NavigationBarHeightDp))
    }
}

//@ComponentPreview
//@Composable
//private fun BandalartEmojiPickerPreview() {
//    BandalartTheme {
//        BandalartEmojiPicker(
//            currentEmoji = "😎",
//            isBottomSheet = false,
//            onEmojiSelect = {},
//        )
//    }
//}
