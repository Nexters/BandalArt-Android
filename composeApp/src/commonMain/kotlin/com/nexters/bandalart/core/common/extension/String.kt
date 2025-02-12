package com.nexters.bandalart.core.common.extension

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.toFormatDate(locale: Locale = Locale.getDefault()): String {
    val dateTime = LocalDateTime.parse(this)
    val year = dateTime.year
    val month = dateTime.monthValue
    val day = dateTime.dayOfMonth

    return when (locale.language) {
        "en" -> "~$year, $month/$day"
        "ko" -> "~${year}년 ${month}월 ${day}일"
        else -> "~${year}y $month/$day"
    }
}

fun String.toStringLocalDateTime(locale: Locale = Locale.getDefault()): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", locale)
    val dueDate = LocalDateTime.parse(this.substring(0, 16), formatter)
    return when (locale.language) {
        "en" -> "${dueDate.year}, ${dueDate.monthValue}/${dueDate.dayOfMonth}"
        "ko" -> "${dueDate.year}년 ${dueDate.monthValue}월 ${dueDate.dayOfMonth}일"
        else -> "${dueDate.year} ${dueDate.monthValue}/${dueDate.dayOfMonth}"
    }
}

fun String.toLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    return LocalDateTime.parse(this.substring(0, 16), formatter)
}

fun String.toColor(): Color {
    return Color(android.graphics.Color.parseColor(this))
}
