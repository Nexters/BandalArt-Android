package com.nexters.bandalart.android.domain.usecase

import com.nexters.bandalart.android.domain.repository.GuestLoginTokenRepository

class SetGuestLoginTokenUseCaseImpl(private val repository: GuestLoginTokenRepository) : SetGuestLoginTokenUseCase {
  override suspend fun invoke(guestLoginToken: String) {
    repository.setGuestLoginToken(guestLoginToken)
  }
}
