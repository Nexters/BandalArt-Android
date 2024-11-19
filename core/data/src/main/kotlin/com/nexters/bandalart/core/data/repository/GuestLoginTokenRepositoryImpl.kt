package com.nexters.bandalart.core.data.repository

import com.nexters.bandalart.core.datastore.datasource.GuestLoginLocalDataSource
import com.nexters.bandalart.core.data.datasource.GuestLoginRemoteDataSource
import com.nexters.bandalart.core.data.mapper.toEntity
import com.nexters.bandalart.core.domain.entity.GuestLoginTokenEntity
import com.nexters.bandalart.core.domain.repository.GuestLoginTokenRepository
import javax.inject.Inject

internal class GuestLoginTokenRepositoryImpl @Inject constructor(
    private val localDataSource: GuestLoginLocalDataSource,
    private val remoteDataSource: GuestLoginRemoteDataSource,
) :
    GuestLoginTokenRepository {
    override suspend fun setGuestLoginToken(guestLoginToken: String) {
        localDataSource.setGuestLoginToken(guestLoginToken)
    }

    override suspend fun getGuestLoginToken(): String {
        return localDataSource.getGuestLoginToken()
    }

    override suspend fun createGuestLoginToken(): GuestLoginTokenEntity? {
        return remoteDataSource.createGuestLoginToken()?.toEntity()
    }
}
