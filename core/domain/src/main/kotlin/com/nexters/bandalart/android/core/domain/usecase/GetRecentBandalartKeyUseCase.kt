package com.nexters.bandalart.android.core.domain.usecase

import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import javax.inject.Inject

class GetRecentBandalartKeyUseCase @Inject constructor(
  private val repository: BandalartRepository,
) {
  suspend operator fun invoke(): String {
    return repository.getRecentBandalartKey()
  }
}
