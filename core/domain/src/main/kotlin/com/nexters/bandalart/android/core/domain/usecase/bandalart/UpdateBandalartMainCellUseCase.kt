package com.nexters.bandalart.android.core.domain.usecase.bandalart

import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartMainCellEntity
import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import com.nexters.bandalart.android.core.domain.util.runSuspendCatching
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateBandalartMainCellUseCase @Inject constructor(
  private val repository: BandalartRepository,
) {
  suspend operator fun invoke(
    bandalartKey: String,
    cellKey: String,
    updateBandalartMainCellEntity: UpdateBandalartMainCellEntity,
  ): Result<Unit> {
    return runSuspendCatching {
      repository.updateBandalartMainCell(bandalartKey, cellKey, updateBandalartMainCellEntity)
    }
  }
}
