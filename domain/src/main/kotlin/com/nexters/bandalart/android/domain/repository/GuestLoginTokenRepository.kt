package com.nexters.bandalart.android.domain.repository

import kotlinx.coroutines.flow.Flow

interface GuestLoginTokenRepository {

  suspend fun setGuestLoginToken(guestLoginToken: String)

  fun getGuestLoginToken(): Flow<Result<String>>
}
