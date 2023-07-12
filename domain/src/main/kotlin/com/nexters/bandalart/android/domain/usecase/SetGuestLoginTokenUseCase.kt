package com.nexters.bandalart.android.domain.usecase

interface SetGuestLoginTokenUseCase {
  suspend operator fun invoke(guestLoginToken: String)
}
