package com.nexters.bandalart.android.core.domain.usecase.bandalart

import com.nexters.bandalart.android.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import com.nexters.bandalart.android.core.domain.util.runSuspendCatching
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val GetBandalartDetailResponseIsNull = IOException("Get BandalartDetail API response is null.")

@Singleton
class GetBandalartDetailUseCase @Inject constructor(
  private val repository: BandalartRepository,
) {
  suspend operator fun invoke(bandalartKey: String): Result<BandalartDetailEntity> =
    runSuspendCatching {
      repository.getBandalartDetail(bandalartKey) ?: throw GetBandalartDetailResponseIsNull
    }
}