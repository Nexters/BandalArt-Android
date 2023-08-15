package com.nexters.bandalart.android.core.domain.usecase.bandalart

import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPrevBandalartListUseCase @Inject constructor(
  private val repository: BandalartRepository,
) {
  suspend operator fun invoke(): List<Pair<String, Boolean>> =
    repository.getPrevBandalartList()
}
