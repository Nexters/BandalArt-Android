package com.nexters.bandalart.android.domain.usecase

import com.nexters.bandalart.android.domain.repository.GuestLoginTokenRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetGuestLoginTokenUseCase @Inject constructor(private val repository: GuestLoginTokenRepository) {
  suspend operator fun invoke(): Flow<Result<String>> {
    return repository.getGuestLoginToken()
  }
}
