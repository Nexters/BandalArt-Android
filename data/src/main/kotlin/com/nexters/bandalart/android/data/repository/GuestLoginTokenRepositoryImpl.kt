package com.nexters.bandalart.android.data.repository

import com.nexters.bandalart.android.data.datasource.GuestLoginTokenDataSource
import com.nexters.bandalart.android.domain.repository.GuestLoginTokenRepository
import kotlinx.coroutines.flow.Flow

class GuestLoginTokenRepositoryImpl(private val dataSource: GuestLoginTokenDataSource) : GuestLoginTokenRepository {
  override suspend fun setGuestLoginToken(guestLoginToken: String) {
    dataSource.set(guestLoginToken)
  }

  override suspend fun getGuestLoginToken(): Flow<Result<String>> {
    return dataSource.get()
  }
}
