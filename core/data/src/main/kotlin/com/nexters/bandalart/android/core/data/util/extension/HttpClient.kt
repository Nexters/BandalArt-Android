package com.nexters.bandalart.android.core.data.util.extension

import com.nexters.bandalart.android.core.data.util.ExceptionWrapper
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import java.nio.channels.UnresolvedAddressException

@Suppress("TooGenericExceptionCaught")
internal suspend fun <T> HttpClient.safeRequest(
  request: suspend HttpClient.() -> T,
): T {
  return try {
    this.request()
  } catch (exception: UnresolvedAddressException) {
    throw ExceptionWrapper(message = exception.toAlertMessage(), cause = exception)
  } catch (exception: ResponseException) {
    throw ExceptionWrapper(
      statusCode = exception.response.status.value,
      message = exception.toAlertMessage(),
      cause = exception,
    )
  } catch (exception: Exception) {
    throw ExceptionWrapper(message = exception.toAlertMessage(), cause = exception)
  }
}
