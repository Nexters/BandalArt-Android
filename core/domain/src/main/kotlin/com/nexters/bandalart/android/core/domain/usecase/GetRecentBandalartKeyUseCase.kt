package com.nexters.bandalart.android.core.domain.usecase

import com.nexters.bandalart.android.core.domain.repository.RecentBandalartKeyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetRecentBandalartKeyUseCase @Inject constructor(private val repository: RecentBandalartKeyRepository) {
  operator fun invoke(): Flow<Result<String>> {
    return repository.getRecentBandalartKey()
  }
}
