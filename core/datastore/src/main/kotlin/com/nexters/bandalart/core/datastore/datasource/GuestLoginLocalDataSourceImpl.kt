package com.nexters.bandalart.core.datastore.datasource

import com.nexters.bandalart.core.datastore.BandalartDataStore
import javax.inject.Inject

internal class GuestLoginLocalDataSourceImpl @Inject constructor(
    private val datastoreBandalart: BandalartDataStore,
) : GuestLoginLocalDataSource {

    override suspend fun setGuestLoginToken(guestLoginToken: String) {
        datastoreBandalart.setGuestLoginToken(guestLoginToken)
    }

    override suspend fun getGuestLoginToken(): String {
        return datastoreBandalart.getGuestLoginToken()
    }
}
