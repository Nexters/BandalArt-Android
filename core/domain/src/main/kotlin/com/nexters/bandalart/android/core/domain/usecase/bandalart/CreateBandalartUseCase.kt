package com.nexters.bandalart.android.core.domain.usecase.bandalart

import com.nexters.bandalart.android.core.domain.entity.BandalartEntity
import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import com.nexters.bandalart.android.core.domain.util.runSuspendCatching
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val CreateBandalartResponseIsNull = IOException("Create Bandalart API response is null.")

@Singleton
class CreateBandalartUseCase @Inject constructor(
  private val repository: BandalartRepository,
) {
  suspend operator fun invoke(): Result<BandalartEntity> =
    runSuspendCatching {
      repository.createBandalart() ?: throw CreateBandalartResponseIsNull
    }
}

