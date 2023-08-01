package com.nexters.bandalart.android.core.data.repository

import com.nexters.bandalart.android.core.data.util.ApiException
import com.nexters.bandalart.android.core.domain.repository.ServerHealthCheckRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import javax.inject.Inject

class ServerHealthCheckRepositoryImpl @Inject constructor(
  private val client: HttpClient,
) : ServerHealthCheckRepository {
  override suspend fun checkServerHealth() {
    try {
      client
        .get("/health-check")
    } catch (e: ResponseException) {
      throw ApiException(e.response.status.value, e.response.status.description)
    }
  }
}
