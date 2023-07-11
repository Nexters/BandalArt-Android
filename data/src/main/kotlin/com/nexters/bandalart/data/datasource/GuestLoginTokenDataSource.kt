package com.nexters.bandalart.data.datasource

import kotlinx.coroutines.flow.Flow

interface GuestLoginTokenDataSource {
  suspend fun set(guestLoginToken: String)
  suspend fun get(): Flow<Result<String>>
}
