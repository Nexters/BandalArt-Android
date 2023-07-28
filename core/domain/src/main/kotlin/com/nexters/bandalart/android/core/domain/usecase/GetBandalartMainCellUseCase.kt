package com.nexters.bandalart.android.core.domain.usecase

import com.nexters.bandalart.android.core.domain.entity.BandalartMainCellEntity
import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import com.nexters.bandalart.android.core.domain.util.runSuspendCatching
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val GetBandalartMainCellResponseIsNull = IOException("Get BandalartMainCell API response is null.")

@Singleton
class GetBandalartMainCellUseCase @Inject constructor(
  private val repository: BandalartRepository,
) {
  suspend operator fun invoke(bandalartKey: String): Result<BandalartMainCellEntity> =
    runSuspendCatching {
      repository.getBandalartMainCell(bandalartKey) ?: throw GetBandalartMainCellResponseIsNull
    }
}
