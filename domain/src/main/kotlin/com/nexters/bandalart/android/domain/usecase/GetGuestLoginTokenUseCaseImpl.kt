package com.nexters.bandalart.android.domain.usecase

import com.nexters.bandalart.android.domain.repository.GuestLoginTokenRepository
import kotlinx.coroutines.flow.Flow

class GetGuestLoginTokenUseCaseImpl(private val repository: GuestLoginTokenRepository) : GetGuestLoginTokenUseCase {
  override suspend fun invoke(): Flow<Result<String>> {
    return repository.getGuestLoginToken()
  }
}
