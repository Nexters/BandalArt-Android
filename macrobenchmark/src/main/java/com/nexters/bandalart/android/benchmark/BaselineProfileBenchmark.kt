package com.nexters.bandalart.android.benchmark

import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class BaselineProfileBenchmark {

  @get:Rule
  val benchmarkRule = MacrobenchmarkRule()

  @Test
  fun startup() = benchmarkRule.measureRepeated(
    packageName = "com.nexters.bandalart.android",
    metrics = listOf(StartupTimingMetric()),
    iterations = 10,
    setupBlock = {
      // Press home button before each run to ensure the starting activity isn't visible.
      pressHome()
    }
  ) {
    // starts default launch activity
    startActivityAndWait()
  }
}
