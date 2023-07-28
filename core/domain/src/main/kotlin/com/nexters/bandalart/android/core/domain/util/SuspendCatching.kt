@file:OptIn(ExperimentalContracts::class)

package com.nexters.bandalart.android.core.domain.util

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException
import kotlin.contracts.ExperimentalContracts

internal inline fun <T> runSuspendCatching(block: () -> T): Result<T> {
  contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
  return runCatching(block).also { result ->
    val maybeException = result.exceptionOrNull()
    if (maybeException is CancellationException) throw maybeException
  }
}
