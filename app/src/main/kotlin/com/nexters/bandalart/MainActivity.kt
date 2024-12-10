package com.nexters.bandalart

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.nexters.bandalart.core.common.utils.isValidImmediateAppUpdate
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.domain.repository.InAppUpdateRepository
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.home.BuildConfig
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
    private val currentVersionCode = BuildConfig.VERSION_CODE

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

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        setupInAppUpdate()
        setContent {
            BandalartTheme {
                BandalartApp()
            }
        }
    }

    private fun setupInAppUpdate() {
        if (currentUpdateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.registerListener(installStateUpdatedListener)
        }

        checkForAppUpdates()
    }

    // TODO 이거 옮겨야 함(선택 업데이트시 사용)
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

    private fun checkForAppUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            val isUpdateAvailable = appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE

            if (isUpdateAvailable) {
                processUpdate(appUpdateInfo)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                processUpdate(appUpdateInfo)
            }
        }
    }

    private fun processUpdate(appUpdateInfo: AppUpdateInfo) {
        val availableVersionCode = appUpdateInfo.availableVersionCode()

        lifecycleScope.launch {
            // 선택 업데이트이고 이전에 업데이트를 거절한 버전인 경우, 업데이트를 표시하지 않음
            if (!isValidImmediateAppUpdate(availableVersionCode.toString(), currentVersionCode) &&
                inAppUpdateRepository.isUpdateAlreadyRejected(availableVersionCode)
            ) {
                return@launch
            }

            val updateType = if (isValidImmediateAppUpdate(availableVersionCode.toString(), currentVersionCode)) {
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

    override fun onDestroy() {
        super.onDestroy()

        if (currentUpdateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }
}
