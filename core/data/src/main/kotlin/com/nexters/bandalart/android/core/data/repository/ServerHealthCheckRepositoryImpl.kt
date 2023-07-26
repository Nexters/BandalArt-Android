package com.nexters.bandalart.android.core.data.repository

import com.nexters.bandalart.android.core.domain.repository.ServerHealthCheckRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject

class ServerHealthCheckRepositoryImpl @Inject constructor(
  private val client: HttpClient,
) : ServerHealthCheckRepository {
  override suspend fun checkServerHealth() {
    client
      .get("/health-check")
  }
}
