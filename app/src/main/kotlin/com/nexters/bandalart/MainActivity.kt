package com.nexters.bandalart

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.domain.repository.InAppUpdateRepository
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.ui.BandalartApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var inAppUpdateRepository: InAppUpdateRepository

    private lateinit var appUpdateManager: AppUpdateManager
    private var currentUpdateType: Int? = null

    private val appUpdateResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult(),
    ) { result: ActivityResult ->
        when (result.resultCode) {
            RESULT_OK -> {
                Timber.d("Update completed")
            }

            RESULT_CANCELED -> {
                Timber.d("Update canceled")
                if (currentUpdateType == AppUpdateType.FLEXIBLE) {
                    lifecycleScope.launch {
                        appUpdateManager.appUpdateInfo.await().availableVersionCode().let { versionCode ->
                            inAppUpdateRepository.setLastRejectedUpdateVersion(versionCode)
                        }
                    }
                } else if (currentUpdateType == AppUpdateType.IMMEDIATE) {
                    finish()
                }
                currentUpdateType = null
            }

            else -> {
                Timber.e("Update failed")
                if (currentUpdateType == AppUpdateType.IMMEDIATE) {
                    finish()
                }
                currentUpdateType = null
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setupBackPressHandler()
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        setupInAppUpdate()
        setContent {
            BandalartTheme {
                BandalartApp()
            }
        }
    }

    private fun setupInAppUpdate() {
        appUpdateManager.registerListener(installStateUpdatedListener)
        checkForAppUpdates()
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // 강제 업데이트일 경우, 뒤로가기 동작이 수행되지 않도록
                    if (currentUpdateType == AppUpdateType.IMMEDIATE) {
                        return
                    }
                    // 선택 업데이트일 경우, 기본 뒤로가기 동작 수행
                    onBackPressedDispatcher.onBackPressed()
                }
            },
        )
    }

    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        when (state.installStatus()) {
            InstallStatus.DOWNLOADED -> {
                Toast.makeText(applicationContext, getString(R.string.update_download_complete), Toast.LENGTH_LONG).show()

                lifecycleScope.launch {
                    delay(5.seconds)
                    appUpdateManager.completeUpdate()
                }
            }

            InstallStatus.FAILED -> {
                currentUpdateType = null
                Timber.e("in-app update failed")
            }

            InstallStatus.CANCELED -> {
                currentUpdateType = null
                Timber.e("in-app update canceled")
            }
        }
    }

    private fun isValidImmediateAppUpdate(updateVersionCode: Int): Boolean {
        // Major 버전 비교 (앞 2자리)
        val updateMajor = updateVersionCode / 10_000
        val currentMajor = BuildConfig.VERSION_CODE / 10_000

        // Minor 버전 비교 (중간 2자리)
        val updateMinor = (updateVersionCode % 10_000) / 100
        val currentMinor = (BuildConfig.VERSION_CODE % 10_000) / 100

        return updateMajor > currentMajor || updateMinor > currentMinor
    }

    private fun checkForAppUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            val isUpdateAvailable = appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE

            if (isUpdateAvailable) {
                val availableVersionCode = appUpdateInfo.availableVersionCode()

                lifecycleScope.launch {
                    // 선택 업데이트이고 이전에 거절한 버전인 경우, 업데이트 표시하지 않음
                    if (!isValidImmediateAppUpdate(availableVersionCode) &&
                        inAppUpdateRepository.isUpdateAlreadyRejected(availableVersionCode)
                    ) {
                        return@launch
                    }

                    val updateType = if (isValidImmediateAppUpdate(availableVersionCode)) {
                        AppUpdateType.IMMEDIATE
                    } else {
                        AppUpdateType.FLEXIBLE
                    }

                    if (appUpdateInfo.isUpdateTypeAllowed(updateType)) {
                        currentUpdateType = updateType
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            appUpdateResultLauncher,
                            AppUpdateOptions.newBuilder(updateType).build(),
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                val availableVersionCode = appUpdateInfo.availableVersionCode()

                lifecycleScope.launch {
                    // 선택 업데이트이고 이전에 거절한 버전인 경우, 업데이트 표시하지 않음
                    if (!isValidImmediateAppUpdate(availableVersionCode) &&
                        inAppUpdateRepository.isUpdateAlreadyRejected(availableVersionCode)
                    ) {
                        return@launch
                    }

                    val updateType = if (isValidImmediateAppUpdate(availableVersionCode)) {
                        AppUpdateType.IMMEDIATE
                    } else {
                        AppUpdateType.FLEXIBLE
                    }

                    if (appUpdateInfo.isUpdateTypeAllowed(updateType)) {
                        currentUpdateType = updateType
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            appUpdateResultLauncher,
                            AppUpdateOptions.newBuilder(updateType).build(),
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        appUpdateManager.unregisterListener(installStateUpdatedListener)
    }
}
