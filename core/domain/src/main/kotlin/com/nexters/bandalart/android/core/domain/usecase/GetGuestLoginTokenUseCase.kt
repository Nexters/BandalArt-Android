package com.nexters.bandalart.android.core.domain.usecase

import com.nexters.bandalart.android.core.domain.repository.GuestLoginTokenRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetGuestLoginTokenUseCase @Inject constructor(private val repository: GuestLoginTokenRepository) {
  operator fun invoke(): Flow<String> {
    return repository.getGuestLoginToken()
  }
}
