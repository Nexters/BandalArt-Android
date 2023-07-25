package com.nexters.bandalart.android.core.domain.usecase

import com.nexters.bandalart.android.core.domain.repository.ServerHealthCheckRepository
import javax.inject.Inject

class CheckServerHealthUseCase @Inject constructor(
  private val repository: ServerHealthCheckRepository,
) {
  suspend operator fun invoke() {
    return repository.checkServerHealth()
  }
}
