package com.nexters.bandalart.android.core.domain.usecase

import com.nexters.bandalart.android.core.domain.repository.GuestLoginTokenRepository
import javax.inject.Inject

class SetGuestLoginTokenUseCase @Inject constructor(private val repository: GuestLoginTokenRepository) {
  suspend operator fun invoke(guestLoginToken: String) {
    repository.setGuestLoginToken(guestLoginToken)
  }
}
