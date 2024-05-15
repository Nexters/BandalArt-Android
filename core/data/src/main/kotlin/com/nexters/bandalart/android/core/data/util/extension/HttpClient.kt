package com.nexters.bandalart.android.core.data.util.extension

// @Suppress("TooGenericExceptionCaught")
// internal suspend fun <T> HttpClient.safeRequest(
//   request: suspend HttpClient.() -> T,
// ): T {
//   return try {
//     this.request()
//   } catch (exception: UnresolvedAddressException) {
//     throw ExceptionWrapper(message = exception.toAlertMessage(), cause = exception)
//   } catch (exception: ResponseException) {
//     throw ExceptionWrapper(
//       statusCode = exception.response.status.value,
//       message = exception.toAlertMessage(),
//       cause = exception,
//     )
//   } catch (exception: Exception) {
//     throw ExceptionWrapper(message = exception.toAlertMessage(), cause = exception)
//   }
// }

// @Suppress("TooGenericExceptionCaught")
// internal suspend fun <T> safeRequest(request: suspend () -> Response<T>): T? {
//   try {
//     val response = request()
//     if (response.isSuccessful) {
//       return response.body()
//     } else {
//       val errorBody = response.errorBody()?.string() ?: "Unknown error"
//       Timber.d(Exception(errorBody))
//       throw ExceptionWrapper(
//         statusCode = response.code(),
//         message = Exception(errorBody).toAlertMessage(),
//         cause = Exception(errorBody),
//       )
//     }
//   } catch (exception: HttpException) {
//     Timber.d(exception)
//     throw ExceptionWrapper(
//       statusCode = exception.code(),
//       message = exception.response()?.errorBody()?.string() ?: exception.message(),
//       cause = exception,
//     )
//   } catch (exception: UnknownHostException) {
//     Timber.d(exception)
//     throw ExceptionWrapper(message = exception.toAlertMessage(), cause = exception)
//   } catch (exception: Exception) {
//     Timber.d(exception)
//     throw ExceptionWrapper(message = exception.toAlertMessage(), cause = exception)
//   }
// }
