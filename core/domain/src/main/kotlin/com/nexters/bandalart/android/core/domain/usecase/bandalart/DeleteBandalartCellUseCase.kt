package com.nexters.bandalart.android.core.domain.usecase.bandalart

import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import com.nexters.bandalart.android.core.domain.util.runSuspendCatching
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteBandalartCellUseCase @Inject constructor(
  private val repository: BandalartRepository,
) {
  suspend operator fun invoke(
    bandalartKey: String,
    cellKey: String,
  ): Result<Unit> {
    return runSuspendCatching {
      repository.deleteBandalartCell(bandalartKey, cellKey)
    }
  }
}
