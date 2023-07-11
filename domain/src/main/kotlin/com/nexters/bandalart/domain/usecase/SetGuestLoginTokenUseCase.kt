package com.nexters.bandalart.domain.usecase

interface SetGuestLoginTokenUseCase {
  suspend operator fun invoke(guestLoginToken: String)
}
