package com.nexters.bandalart.android.core.domain.usecase.bandalart

import com.nexters.bandalart.android.core.domain.entity.BandalartShareEntity
import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import com.nexters.bandalart.android.core.domain.util.runSuspendCatching
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val ShareBandalartResponseIsNull = IOException("Share Bandalart API response is null.")

//@Singleton
//class ShareBandalartUseCase @Inject constructor(
//  private val repository: BandalartRepository,
//) {
//  suspend operator fun invoke(bandalartKey: String): Result<BandalartShareEntity> {
//    return runSuspendCatching {
//      repository.shareBandalart(bandalartKey) ?: throw ShareBandalartResponseIsNull
//    }
//  }
//}
