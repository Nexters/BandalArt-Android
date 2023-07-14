package com.nexters.bandalart.android.data.datasource

import kotlinx.coroutines.flow.Flow

interface GuestLoginTokenDataSource {
  suspend fun setGuestLoginToken(guestLoginToken: String)
  suspend fun getGuestLoginToken(): Flow<Result<String>>
}
