package com.nexters.bandalart.domain.repository

import kotlinx.coroutines.flow.Flow

interface GuestLoginTokenRepository {

  suspend fun setGuestLoginToken(guestLoginToken: String)

  suspend fun getGuestLoginToken(): Flow<Result<String>>
}
