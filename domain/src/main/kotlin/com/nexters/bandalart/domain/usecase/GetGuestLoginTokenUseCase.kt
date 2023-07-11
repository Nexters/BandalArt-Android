package com.nexters.bandalart.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetGuestLoginTokenUseCase {
  suspend operator fun invoke(): Flow<Result<String>>
}
