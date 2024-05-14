package com.nexters.bandalart.android.core.data.datasource

import com.nexters.bandalart.android.core.data.util.extension.safeRequest
import com.nexters.bandalart.android.core.network.model.GuestLoginTokenResponse
import com.nexters.bandalart.android.core.network.service.GuestLoginService
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
