package com.nexters.bandalart.android.core.common

import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface ErrorHandlerActions {
  fun setServerErrorDialogVisible(flag: Boolean)
  fun setNetworkErrorDialogVisible(flag: Boolean)
}

fun handleException(exception: Throwable, actions: ErrorHandlerActions) {
  when (exception) {
    is HttpException -> {
      if (exception.code() in 500..511) {
        actions.setServerErrorDialogVisible(true)
      } else {
        Timber.e(exception)
      }
    }

    is UnknownHostException -> {
      actions.setNetworkErrorDialogVisible(true)
    }

    is SocketTimeoutException -> {
      actions.setServerErrorDialogVisible(true)
    }

    else -> {
      Timber.e(exception)
    }
  }
}
