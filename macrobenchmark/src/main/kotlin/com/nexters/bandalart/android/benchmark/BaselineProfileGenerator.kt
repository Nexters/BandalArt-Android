@file:OptIn(ExperimentalBaselineProfilesApi::class)

package com.nexters.bandalart.android.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
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
  fun startup() {
    baselineProfileRule.collectBaselineProfile(packageName = "com.nexters.bandalart.android") {
      pressHome()
      startActivityAndWait()
    }
  }
}
