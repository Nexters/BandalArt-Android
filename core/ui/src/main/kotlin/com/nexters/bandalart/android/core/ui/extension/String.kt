package com.nexters.bandalart.android.core.ui.extension

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.toColor(): Color {
  return Color(android.graphics.Color.parseColor(this))
}

// ISO 8601 형식의 날짜와 시간 문자열을 "~년 월 일" 형태로 변환
// "2023-08-02T18:27:25.862Z" -> "~23년 8월 2일"
fun String.toFormatDate(): String {
  val instant = Instant.parse(this)
  val dateTime = instant.toLocalDateTime(TimeZone.UTC)
  return "~${dateTime.year - 2000}년 ${dateTime.monthNumber}월 ${dateTime.dayOfMonth}일"
}
