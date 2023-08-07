package com.nexters.bandalart.android.core.domain.usecase.login

import com.nexters.bandalart.android.core.domain.repository.GuestLoginTokenRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetGuestLoginTokenUseCase @Inject constructor(
  private val repository: GuestLoginTokenRepository,
) {
  suspend operator fun invoke(): String {
    return repository.getGuestLoginToken()
  }
}
