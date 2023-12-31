package com.nexters.bandalart.android.core.domain.usecase.bandalart

import com.nexters.bandalart.android.core.domain.entity.BandalartDetailEntity
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
  suspend operator fun invoke(): Result<List<BandalartDetailEntity>> =
    runSuspendCatching {
      repository.getBandalartList() ?: throw GetBandalartListResponseIsNull
    }
}
