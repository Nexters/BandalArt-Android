package com.nexters.bandalart.android.core.data.datasource

import com.nexters.bandalart.android.core.network.model.GuestLoginTokenResponse

interface GuestLoginRemoteDataSource {
  suspend fun createGuestLoginToken(): GuestLoginTokenResponse?
}
