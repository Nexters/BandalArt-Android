package com.nexters.bandalart.data.repository

import com.nexters.bandalart.data.datasource.GuestLoginTokenDataSource
import com.nexters.bandalart.domain.repository.GuestLoginTokenRepository
import kotlinx.coroutines.flow.Flow

class GuestLoginTokenRepositoryImpl(private val dataSource: GuestLoginTokenDataSource) : GuestLoginTokenRepository {
  override suspend fun setGuestLoginToken(guestLoginToken: String) {
    dataSource.set(guestLoginToken)
  }

  override suspend fun getGuestLoginToken(): Flow<Result<String>> {
    return dataSource.get()
  }
}
