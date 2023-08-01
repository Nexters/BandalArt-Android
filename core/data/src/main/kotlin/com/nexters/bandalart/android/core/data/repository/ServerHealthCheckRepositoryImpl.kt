package com.nexters.bandalart.android.core.data.repository

import com.nexters.bandalart.android.core.data.util.ExceptionWrapper
import com.nexters.bandalart.android.core.domain.repository.ServerHealthCheckRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

@Suppress("TooGenericExceptionCaught")
class ServerHealthCheckRepositoryImpl @Inject constructor(
  private val client: HttpClient,
) : ServerHealthCheckRepository {
  override suspend fun checkServerHealth() {
    try {
      client
        .get("/health-check")
    } catch (exception: UnresolvedAddressException) {
      throw ExceptionWrapper(message = "No Internet connection", cause = exception)
    } catch (exception: ResponseException) {
      throw ExceptionWrapper(
        statusCode = exception.response.status.value,
        message = exception.response.status.description,
        cause = exception,
      )
    } catch (exception: Exception) {
      throw ExceptionWrapper(message = "Unexpected Error", cause = exception)
    }
  }
}
