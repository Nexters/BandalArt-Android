package com.nexters.bandalart.android.core.domain.usecase

import com.nexters.bandalart.android.core.domain.entity.BandalartListEntity
import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import com.nexters.bandalart.android.core.domain.util.runSuspendCatching
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val GetBandalartListResponseIsNull = IOException("Get BandalartList API response is null.")

@Singleton
class GetBandalartListUseCase @Inject constructor(
  private val repository: BandalartRepository,
) {
  suspend operator fun invoke(): Result<BandalartListEntity> =
    runSuspendCatching {
      repository.getBandalartList() ?: throw GetBandalartListResponseIsNull
    }
}
