package com.nexters.bandalart.android.core.data.remote.datasource

import com.nexters.bandalart.android.core.data.datasource.GuestLoginRemoteDataSource
import com.nexters.bandalart.android.core.data.model.GuestLoginTokenResponse
import com.nexters.bandalart.android.core.data.service.GuestLoginService
import com.nexters.bandalart.android.core.data.util.extension.safeRequest
import javax.inject.Inject

internal class GuestLoginRemoteDataSourceImpl @Inject constructor(
  private val service: GuestLoginService,
  // private val client: HttpClient,
) : GuestLoginRemoteDataSource {
  override suspend fun createGuestLoginToken(): GuestLoginTokenResponse? {
//    return client.safeRequest {
//      post("v1/users/guests").body()
//    }
    return safeRequest {
      service.createGuestLoginToken()
    }
  }
}
