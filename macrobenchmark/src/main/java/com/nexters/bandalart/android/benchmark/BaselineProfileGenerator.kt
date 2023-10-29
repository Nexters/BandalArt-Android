package com.nexters.bandalart.android.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * All baseline profile methods should be run in no-google-api emulator.
 */

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

  @get:Rule
  val baselineProfileRule = BaselineProfileRule()

  @Test
  fun startUp() {
    baselineProfileRule.collect(
      packageName = "com.nexters.bandalart.android",
      profileBlock = {
        startActivityAndWait()
      }
    )
  }
}
