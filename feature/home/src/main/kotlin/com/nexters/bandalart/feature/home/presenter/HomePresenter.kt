package com.nexters.bandalart.feature.home.presenter

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nexters.bandalart.core.common.extension.bitmapToFileUri
import com.nexters.bandalart.core.common.extension.externalShareForBitmap
import com.nexters.bandalart.core.common.extension.saveImageToGallery
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartMainCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartSubCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartTaskCellEntity
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.core.domain.repository.InAppUpdateRepository
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.complete.CompleteScreen
import com.nexters.bandalart.feature.home.HomeScreen
import com.nexters.bandalart.feature.home.HomeScreen.Event
import com.nexters.bandalart.feature.home.HomeScreen.State
import com.nexters.bandalart.feature.home.mapper.toUiModel
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import com.nexters.bandalart.feature.home.model.CellType
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

// TODO Presenter 에 context 못쓰지 않나? 여기서 이벤트 구현하는게 맞나? -> 쓸 수 있음
// TODO Navigation 을 app 모듈 또는 main 모듈에서 전역으로 관리하는게 아니다보니, feature 모듈간에 순환참조가 발생할 것 같은데...
// TODO Intent 와 SideEffect 가 구분되지 않는다... 어쩌지
class HomePresenter @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted private val navigator: Navigator,
    private val bandalartRepository: BandalartRepository,
    private val inAppUpdateRepository: InAppUpdateRepository,
) : Presenter<State> {
    @Composable
    override fun present(): State {
        var bandalartList by rememberRetained { mutableStateOf(persistentListOf<BandalartUiModel>()) }
        var bandalartData by rememberRetained { mutableStateOf<BandalartUiModel?>(null) }
        var bandalartCellData by rememberRetained { mutableStateOf<BandalartCellEntity?>(null) }
        var bandalartChartUrl by rememberRetained { mutableStateOf("") }
        var isBandalartCompleted by rememberRetained { mutableStateOf(false) }
        var bottomSheet by rememberSaveable { mutableStateOf<HomeScreen.BottomSheetState?>(null) }
        var dialog by rememberSaveable { mutableStateOf<HomeScreen.DialogState?>(null) }
        var isDropDownMenuOpened by rememberRetained { mutableStateOf(false) }
        var isSharing by rememberRetained { mutableStateOf(false) }
        var isCapturing by rememberRetained { mutableStateOf(false) }
        var clickedCellType by rememberRetained { mutableStateOf(CellType.MAIN) }
        var clickedCellData by rememberRetained { mutableStateOf(BandalartCellEntity()) }

        val scope = rememberStableCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        val appVersion = remember {
            try {
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.tag("AppVersion").e(e, "Failed to get package info")
                "Unknown"
            }
        }

        // 반다라트 완료 상태 관찰
        LaunchedEffect(bandalartData) {
            bandalartData?.let { bandalart ->
                if (bandalart.isCompleted && bandalart.title?.isNotEmpty() == true) {
                    val isCompleted = checkCompletedBandalartId(bandalart.id)
                    if (isCompleted) {
                        delay(500L)
                        isCapturing = true
                        delay(500L)
                        navigator.goTo(
                            CompleteScreen(
                                bandalartId = bandalart.id,
                                bandalartTitle = bandalart.title,
                                bandalartProfileEmoji = bandalart.profileEmoji.orEmpty(),
                                bandalartChartImageUri = bandalartChartUrl,
                            )
                        )
                    }
                }
            }
        }

        suspend fun getBandalartMainCell(bandalartId: Long) {
            val mainCell = bandalartRepository.getBandalartMainCell(bandalartId)
            val subCells = bandalartRepository.getChildCells(mainCell?.id ?: 0L)

            val children = subCells.map { subCell ->
                val taskCells = bandalartRepository.getChildCells(subCell.id)
                BandalartCellEntity(
                    id = subCell.id,
                    title = subCell.title,
                    description = subCell.description,
                    dueDate = subCell.dueDate,
                    isCompleted = subCell.isCompleted,
                    parentId = subCell.parentId,
                    children = taskCells.map { taskCell ->
                        BandalartCellEntity(
                            id = taskCell.id,
                            title = taskCell.title,
                            description = taskCell.description,
                            dueDate = taskCell.dueDate,
                            isCompleted = taskCell.isCompleted,
                            parentId = taskCell.parentId,
                            children = emptyList(),
                        )
                    },
                )
            }

            bandalartCellData = BandalartCellEntity(
                id = mainCell?.id ?: 0L,
                title = mainCell?.title,
                description = mainCell?.description,
                dueDate = mainCell?.dueDate,
                isCompleted = mainCell?.isCompleted ?: false,
                parentId = mainCell?.parentId,
                children = children,
            )
        }

        suspend fun getBandalart(
            bandalartId: Long,
            isCompleted: Boolean = false,
        ) {
            bandalartRepository.getBandalart(bandalartId).let { bandalart ->
                bandalartData = bandalart.toUiModel()
                bottomSheet = null
                isBandalartCompleted = isCompleted
                getBandalartMainCell(bandalartId)
            }
        }

        suspend fun createBandalart() {
            if ((bandalartList.size + 1) > 5) {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.limit_create_bandalart),
                    duration = SnackbarDuration.Short,
                )
                return
            }

            bandalartRepository.createBandalart()?.let { bandalart ->
                bottomSheet = null
                getBandalart(bandalart.id)
                setRecentBandalartId(bandalart.id)
                upsertBandalartId(bandalart.id, bandalart.isCompleted)
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.create_bandalart),
                    duration = SnackbarDuration.Short,
                )
            }
        }

        // 초기 데이터 로딩
        LaunchedEffect(Unit) {
            bandalartRepository.getBandalartList()
                .map { list -> list.map { it.toUiModel() } }
                .collect { list ->
                    bandalartList = list.toPersistentList()

                    // 이전 반다라트 목록 상태 조회
                    val prevBandalartList = bandalartRepository.getPrevBandalartList()

                    // 새로 업데이트 된 상태와 이전 상태를 비교
                    val completedKeys = list.filter { bandalart ->
                        val prevBandalart = prevBandalartList.find { it.first == bandalart.id }
                        prevBandalart != null && !prevBandalart.second && bandalart.isCompleted
                    }.map { it.id }

                    // 이번에 목표를 달성한 반다라트가 존재하는 경우
                    if (completedKeys.isNotEmpty()) {
                        getBandalart(completedKeys[0], true)
                        return@collect
                    }

                    // 데이터를 동기화
                    list.forEach { bandalart ->
                        bandalartRepository.upsertBandalartId(bandalart.id, bandalart.isCompleted)
                    }

                    // 반다라트 목록이 존재하지 않을 경우, 새로운 반다라트를 생성
                    if (list.isEmpty()) {
                        createBandalart()
                    } else {
                        // 가장 최근에 확인한 반다라트 표를 화면에 띄우는 경우
                        val recentBandalartId = getRecentBandalartId()
                        // 가장 최근에 확인한 반다라트 표가 존재하는 경우
                        if (list.any { it.id == recentBandalartId }) {
                            getBandalart(recentBandalartId)
                        } else {
                            // 목록에 가장 첫번째 표를 화면에 띄움
                            getBandalart(list[0].id)
                        }
                    }
                }
        }

        fun handleEvent(event: Event) {
            when (event) {
                is Event.OnListClick -> {
                    bottomSheet = HomeScreen.BottomSheetState.BandalartList(
                        bandalartList = bandalartList,
                        currentBandalartId = bandalartData?.id ?: return,
                    )
                }

                is Event.OnSaveClick -> {
                    isCapturing = true
                }

                is Event.OnDeleteClick -> {
                    dialog = HomeScreen.DialogState.BandalartDelete
                }

                is Event.OnEmojiSelected -> {
                    scope.launch {
                        bandalartRepository.updateBandalartEmoji(
                            event.bandalartId,
                            event.cellId,
                            event.updateBandalartEmojiModel,
                        )
                        dialog = null
                    }
                }

                is Event.OnShareButtonClick -> {
                    isSharing = true
                }

                is Event.OnAddClick -> {
                    scope.launch {
                        createBandalart()
                    }
                }

                is Event.CaptureBandalart -> {
                    isCapturing = false
                    bandalartChartUrl = context.bitmapToFileUri(event.bitmap).toString()
                }

                is Event.OnAppTitleClick -> {
                    Toast.makeText(context, context.getString(R.string.app_version_info, appVersion), Toast.LENGTH_SHORT).show()
                }

                // TODO 태스크셀이고 상위 서브셀이 비어있을 때(테스트셀이 자신의 부모를 알고있어야 구현 가능 함)
                is Event.OnBandalartCellClick -> {
                    when {
                        event.cellType != CellType.MAIN && bandalartData?.title.isNullOrEmpty() -> {
                            Toast.makeText(context, context.getString(R.string.please_input_main_goal), Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            clickedCellData = event.cellData
                            clickedCellType = event.cellType
                            bottomSheet = bandalartData?.let {
                                HomeScreen.BottomSheetState.Cell(
                                    initialCellData = event.cellData,
                                    cellData = event.cellData,
                                    initialBandalartData = it,
                                    bandalartData = it,
                                )
                            }
                        }
                    }
                }

                is Event.OnBandalartListItemClick -> {
                    scope.launch {
                        bottomSheet = null
                    }
                }

                is Event.OnCancelClick -> {
                    dialog = null
                }

                is Event.OnCellTitleUpdate -> {
                    val currentBottomSheet = bottomSheet as? HomeScreen.BottomSheetState.Cell ?: return
                    val maxLength = when (event.locale.language) {
                        Locale.KOREAN.language, Locale.JAPAN.language -> 15
                        else -> 24
                    }

                    val validatedTitle = if (event.title.length > maxLength) {
                        currentBottomSheet.cellData.title ?: ""
                    } else event.title

                    bottomSheet = currentBottomSheet.copy(
                        cellData = currentBottomSheet.cellData.copy(title = validatedTitle),
                    )
                }

                is Event.OnCloseButtonClick -> {
                    bottomSheet = null
                }

                is Event.OnColorSelect -> {
                    val currentBottomSheet = bottomSheet as? HomeScreen.BottomSheetState.Cell ?: return
                    bottomSheet = currentBottomSheet.copy(
                        bandalartData = currentBottomSheet.bandalartData.copy(
                            mainColor = event.mainColor,
                            subColor = event.subColor,
                        ),
                    )
                }

                is Event.OnCompleteButtonClick -> {
                    scope.launch {
                        val bottomSheetData = bottomSheet as? HomeScreen.BottomSheetState.Cell ?: return@launch
                        val cellData = bottomSheetData.cellData

                        val trimmedTitle = cellData.title?.trim()
                        val description = cellData.description
                        val dueDate = cellData.dueDate?.ifEmpty { null }

                        when (event.cellType) {
                            CellType.MAIN -> {
                                updateMainCell(
                                    bandalartId = event.bandalartId,
                                    cellId = event.cellId,
                                    updateBandalartMainCellModel = UpdateBandalartMainCellEntity(
                                        title = trimmedTitle,
                                        description = description,
                                        dueDate = dueDate,
                                        profileEmoji = bottomSheetData.bandalartData.profileEmoji,
                                        mainColor = bottomSheetData.bandalartData.mainColor,
                                        subColor = bottomSheetData.bandalartData.subColor,
                                    ),
                                )
                            }

                            CellType.SUB -> {
                                updateSubCell(
                                    bandalartId = event.bandalartId,
                                    cellId = event.cellId,
                                    updateBandalartSubCellModel = UpdateBandalartSubCellEntity(
                                        title = trimmedTitle,
                                        description = description,
                                        dueDate = dueDate,
                                    ),
                                )
                            }

                            else -> {
                                updateTaskCell(
                                    bandalartId = event.bandalartId,
                                    cellId = event.cellId,
                                    updateBandalartTaskCellModel = UpdateBandalartTaskCellEntity(
                                        title = trimmedTitle,
                                        description = description,
                                        dueDate = dueDate,
                                        isCompleted = cellData.isCompleted,
                                    ),
                                )
                            }
                        }
                    }
                }

                is Event.OnCompletionUpdate -> {
                    val currentBottomSheet = (bottomSheet as? HomeScreen.BottomSheetState.Cell) ?: return
                    bottomSheet = currentBottomSheet.copy(
                        cellData = currentBottomSheet.cellData.copy(isCompleted = event.isCompleted),
                    )
                }

                is Event.OnDatePickerClick -> {
                    val currentSheet = bottomSheet as? HomeScreen.BottomSheetState.Cell ?: return
                    bottomSheet = currentSheet.copy(
                        isDatePickerOpened = true,
                    )
                }

                is Event.OnDeleteBandalart -> {
                    scope.launch {
                        bandalartRepository.deleteBandalart(event.bandalartId)
                        dialog = null
                        bottomSheet = null
                        isDropDownMenuOpened = false
                        deleteBandalartId(event.bandalartId)
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.delete_bandalart),
                            duration = SnackbarDuration.Short,
                        )
                    }
                }

                is Event.OnDeleteButtonClick -> {
                    dialog = HomeScreen.DialogState.CellDelete(
                        cellType = clickedCellType,
                        cellTitle = (bottomSheet as? HomeScreen.BottomSheetState.Cell)?.cellData?.title,
                    )
                }

                is Event.OnDeleteCell -> {
                    scope.launch {
                        bandalartRepository.deleteBandalartCell(event.cellId)
                    }
                }

                is Event.OnDescriptionUpdate -> {
                    val currentBottomSheet = bottomSheet as? HomeScreen.BottomSheetState.Cell ?: return
                    val validatedDescription = if (event.description.length > 1000) {
                        currentBottomSheet.cellData.description
                    } else {
                        event.description
                    }
                    bottomSheet = currentBottomSheet.copy(
                        cellData = currentBottomSheet.cellData.copy(description = validatedDescription),
                    )
                }

                is Event.OnDismiss -> {
                    bottomSheet = null
                }

                is Event.OnDropDownMenuDismiss -> {
                    isDropDownMenuOpened = false
                }

                is Event.OnDueDateSelect -> {
                    val currentBottomSheet = bottomSheet as? HomeScreen.BottomSheetState.Cell ?: return
                    bottomSheet = currentBottomSheet.copy(
                        cellData = currentBottomSheet.cellData.copy(dueDate = event.date),
                    )
                }

                is Event.OnEmojiClick -> {
                    bandalartData?.let { bandalartData ->
                        bottomSheet = HomeScreen.BottomSheetState.Emoji(
                            bandalartId = bandalartData.id,
                            cellId = bandalartData.id,
                            currentEmoji = bandalartData.profileEmoji,
                        )
                    }
                }

                is Event.OnEmojiPickerClick -> {
                    val currentBottomSheet = bottomSheet as? HomeScreen.BottomSheetState.Cell ?: return
                    bottomSheet = currentBottomSheet.copy(
                        isEmojiPickerOpened = true,
                    )
                }

                is Event.OnEmojiSelect -> {
                    val currentBottomSheet = bottomSheet as? HomeScreen.BottomSheetState.Emoji ?: return
                    bottomSheet = currentBottomSheet.copy(
                        currentEmoji = event.emoji,
                    )
                }

                is Event.OnMenuClick -> {
                    isDropDownMenuOpened = true
                }

                is Event.OnShareRequested -> {
                    scope.launch {
                        isSharing = false
                        context.externalShareForBitmap(event.bitmap)
                    }
                }

                is Event.OnSaveRequested -> {
                    scope.launch {
                        isCapturing = false
                        context.saveImageToGallery(event.bitmap)
                        Toast.makeText(
                            context,
                            context.getString(R.string.save_bandalart_image),
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

                is Event.OnCaptureRequested -> {
                    scope.launch {
                        isCapturing = false
                        context.bitmapToFileUri(event.bitmap)?.let { uri ->
                            bandalartChartUrl = uri.toString()
                        }
                    }
                }

                is Event.ShowAppVersion -> {
                    Toast.makeText(context, context.getString(R.string.app_version_info, appVersion), Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun updateBandalartChartImageUri(url: String) {
            bandalartChartUrl = url
        }

        return State(
            bandalartList = bandalartList,
            bandalartData = bandalartData,
            bandalartCellData = bandalartCellData,
            bandalartChartUrl = bandalartChartUrl,
            isBandalartCompleted = isBandalartCompleted,
            bottomSheet = bottomSheet,
            dialog = dialog,
            isSharing = isSharing,
            isCapturing = isCapturing,
            isDropDownMenuOpened = isDropDownMenuOpened,
            clickedCellType = clickedCellType,
            clickedCellData = clickedCellData,
            eventSink = { event -> handleEvent(event) },
        )
    }

    private suspend fun updateMainCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartMainCellModel: UpdateBandalartMainCellEntity,
    ) {
        bandalartRepository.updateBandalartMainCell(bandalartId, cellId, updateBandalartMainCellModel)
        bandalartRepository.getBandalart(bandalartId)
    }

    private suspend fun updateSubCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartSubCellModel: UpdateBandalartSubCellEntity,
    ) {
        bandalartRepository.updateBandalartSubCell(bandalartId, cellId, updateBandalartSubCellModel)
    }

    private suspend fun updateTaskCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartTaskCellModel: UpdateBandalartTaskCellEntity,
    ) {
        bandalartRepository.updateBandalartTaskCell(bandalartId, cellId, updateBandalartTaskCellModel)
    }

    private suspend fun getRecentBandalartId(): Long {
        return bandalartRepository.getRecentBandalartId()
    }

    private suspend fun setRecentBandalartId(bandalartId: Long) {
        bandalartRepository.setRecentBandalartId(bandalartId)
    }

    private suspend fun upsertBandalartId(
        bandalartId: Long,
        isCompleted: Boolean = false,
    ) {
        bandalartRepository.upsertBandalartId(bandalartId, isCompleted)
    }

    private suspend fun checkCompletedBandalartId(bandalartId: Long): Boolean {
        return bandalartRepository.checkCompletedBandalartId(bandalartId)
    }

    private suspend fun deleteBandalartId(bandalartId: Long) {
        bandalartRepository.deleteCompletedBandalartId(bandalartId)
    }

    suspend fun setLastRejectedUpdateVersion(versionCode: Int) {
        inAppUpdateRepository.setLastRejectedUpdateVersion(versionCode)
    }

    suspend fun isUpdateAlreadyRejected(versionCode: Int): Boolean {
        return inAppUpdateRepository.isUpdateAlreadyRejected(versionCode)
    }

    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }
}
