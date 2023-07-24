package com.nexters.bandalart.android.core.domain.usecase

import com.nexters.bandalart.android.core.domain.repository.RecentBandalartKeyRepository
import javax.inject.Inject

class SetRecentBandalartKeyUseCase @Inject constructor(private val repository: RecentBandalartKeyRepository) {
  suspend operator fun invoke(recentBandalartKey: String) {
    repository.setRecentBandalartKey(recentBandalartKey)
  }
}
