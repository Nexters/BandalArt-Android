package com.nexters.bandalart.android.core.domain.usecase

import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartCellEntity
import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import com.nexters.bandalart.android.core.domain.util.runSuspendCatching
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateBandalartCellUseCase @Inject constructor(
  private val repository: BandalartRepository,
) {
  suspend operator fun invoke(
    bandalartKey: String,
    cellKey: String,
    updateBandalartCellEntity: UpdateBandalartCellEntity,
  ) {
    runSuspendCatching {
      repository.updateBandalartCell(bandalartKey, cellKey, updateBandalartCellEntity)
    }
  }
}
