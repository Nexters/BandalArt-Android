package com.nexters.bandalart.android.core.domain.usecase.bandalart

import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import com.nexters.bandalart.android.core.domain.util.runSuspendCatching
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteBandalartUseCase @Inject constructor(
  private val repository: BandalartRepository,
) {
  suspend operator fun invoke(bandalartKey: String) {
    runSuspendCatching {
      repository.deleteBandalart(bandalartKey)
    }
  }
}
