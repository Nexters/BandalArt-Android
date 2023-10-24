package com.nexters.bandalart.android.core.domain

import com.nexters.bandalart.android.core.domain.util.runSuspendCatching
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.result.shouldBeFailure
import kotlin.coroutines.cancellation.CancellationException

class SuspendCatchingTest : StringSpec() {
  init {
    "CancellationException은 throw됨" {
      val throwing = { throw CancellationException() }

      shouldThrow<CancellationException> {
        runSuspendCatching(throwing)
      }
    }

    "CancellationException을 제외한 다른 Exception은 FailureResult로 래핑됨" {
      val throwing = { throw object : Exception() {} }
      val result = runSuspendCatching(throwing)

      result.shouldBeFailure()
    }
  }
}