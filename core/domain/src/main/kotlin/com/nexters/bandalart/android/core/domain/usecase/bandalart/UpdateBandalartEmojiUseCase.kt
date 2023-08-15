package com.nexters.bandalart.android.core.domain.usecase.bandalart

import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import com.nexters.bandalart.android.core.domain.util.runSuspendCatching
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateBandalartEmojiUseCase @Inject constructor(
  private val repository: BandalartRepository,
) {
  suspend operator fun invoke(
    bandalartKey: String,
    cellKey: String,
    updateBandalartEmojiEntity: UpdateBandalartEmojiEntity,
  ): Result<Unit> {
    return runSuspendCatching {
      repository.updateBandalartEmoji(bandalartKey, cellKey, updateBandalartEmojiEntity)
    }
  }
}
