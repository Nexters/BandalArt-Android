package com.nexters.bandalart.android.core.domain.usecase.bandalart

import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteCompletedBandalartKeyUseCase @Inject constructor(
  private val repository: BandalartRepository,
) {
  suspend operator fun invoke(bandalartKey: String) {
    repository.deleteCompletedBandalartKey(bandalartKey)
  }
}
