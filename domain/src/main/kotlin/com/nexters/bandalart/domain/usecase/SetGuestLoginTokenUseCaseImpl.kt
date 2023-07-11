package com.nexters.bandalart.domain.usecase

import com.nexters.bandalart.domain.repository.GuestLoginTokenRepository

class SetGuestLoginTokenUseCaseImpl(private val repository: GuestLoginTokenRepository) : SetGuestLoginTokenUseCase {
  override suspend fun invoke(guestLoginToken: String) {
    repository.setGuestLoginToken(guestLoginToken)
  }
}
