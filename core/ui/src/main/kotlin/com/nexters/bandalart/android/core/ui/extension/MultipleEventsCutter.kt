package com.nexters.bandalart.android.core.ui.extension

internal interface MultipleEventsCutter {
  fun processEvent(event: () -> Unit)

  companion object
}

internal fun MultipleEventsCutter.Companion.get(): MultipleEventsCutter =
  MultipleEventsCutterImpl()

private class MultipleEventsCutterImpl : MultipleEventsCutter {
  private val now: Long
    get() = System.currentTimeMillis()

  private var lastEventTimeMs: Long = 0

  override fun processEvent(event: () -> Unit) {
    if (now - lastEventTimeMs >= 500L) {
      event.invoke()
    }
    lastEventTimeMs = now
  }
}
