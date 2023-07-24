package com.nexters.bandalart.android.core.data.datasource

import kotlinx.coroutines.flow.Flow

interface GuestLoginTokenDataSource {
  suspend fun setGuestLoginToken(guestLoginToken: String)
  fun getGuestLoginToken(): Flow<String>
}
