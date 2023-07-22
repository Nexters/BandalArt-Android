package com.nexters.bandalart.android.domain.usecase

import com.nexters.bandalart.android.domain.repository.GuestLoginTokenRepository
import javax.inject.Inject

class SetGuestLoginTokenUseCase @Inject constructor(private val repository: GuestLoginTokenRepository) {
  suspend operator fun invoke(guestLoginToken: String) {
    repository.setGuestLoginToken(guestLoginToken)
  }
}
