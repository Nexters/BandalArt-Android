package com.nexters.bandalart.android.core.datastore.datasource

import com.nexters.bandalart.android.core.datastore.DataStoreProvider
import javax.inject.Inject

internal class GuestLoginLocalDataSourceImpl @Inject constructor(
    private val datastoreProvider: DataStoreProvider,
) : GuestLoginLocalDataSource {

    override suspend fun setGuestLoginToken(guestLoginToken: String) {
        datastoreProvider.setGuestLoginToken(guestLoginToken)
    }

    override suspend fun getGuestLoginToken(): String {
        return datastoreProvider.getGuestLoginToken()
    }
}
