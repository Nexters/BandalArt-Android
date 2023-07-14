package com.nexters.bandalart.android.data.repository

import com.nexters.bandalart.android.data.datasource.GuestLoginTokenDataSource
import com.nexters.bandalart.android.domain.repository.GuestLoginTokenRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GuestLoginTokenRepositoryImpl @Inject constructor(private val dataSource: GuestLoginTokenDataSource) : GuestLoginTokenRepository {
  override suspend fun setGuestLoginToken(guestLoginToken: String) {
    dataSource.setGuestLoginToken(guestLoginToken)
  }

  override suspend fun getGuestLoginToken(): Flow<Result<String>> {
    return dataSource.getGuestLoginToken()
  }
}
