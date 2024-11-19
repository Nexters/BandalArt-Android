package com.nexters.bandalart.core.data.datasource

import com.nexters.bandalart.core.network.model.GuestLoginTokenResponse

interface GuestLoginRemoteDataSource {
    suspend fun createGuestLoginToken(): GuestLoginTokenResponse?
}
