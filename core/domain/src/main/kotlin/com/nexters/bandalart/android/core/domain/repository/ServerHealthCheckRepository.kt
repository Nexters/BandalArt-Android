package com.nexters.bandalart.android.core.domain.repository

interface ServerHealthCheckRepository {
  suspend fun checkServerHealth()
}
