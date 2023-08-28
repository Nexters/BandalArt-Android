package com.nexters.bandalart.android.core.data.util.extension

import java.net.UnknownHostException
import retrofit2.HttpException

// internal fun Exception.toAlertMessage(): String {
//   return when (this) {
//     is UnresolvedAddressException -> "네트워크 연결을 확인해주세요."
//     is ResponseException -> "서버에 문제가 발생했어요. 잠시 후 다시 시도해주세요."
//     else -> "예기치 못한 오류가 발생했어요. 잠시 후 다시 시도해주세요."
//   }
// }

internal fun Exception.toAlertMessage(): String {
  return when (this) {
    is HttpException -> "서버에 문제가 발생했어요. 잠시 후 다시 시도해주세요."
    is UnknownHostException -> "네트워크 연결을 확인해주세요."
    else -> "예기치 못한 오류가 발생했어요. 잠시 후 다시 시도해주세요."
  }
}
