package com.nexters.bandalart.feature.home.presenter

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import com.nexters.bandalart.core.common.extension.bitmapToFileUri
import com.nexters.bandalart.core.common.extension.externalShareForBitmap
import com.nexters.bandalart.core.common.extension.saveImageToGallery
import com.nexters.bandalart.core.common.utils.UiText
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartMainCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartSubCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartTaskCellEntity
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.core.domain.repository.InAppUpdateRepository
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.home.HomeScreen
import com.nexters.bandalart.feature.home.HomeScreen.Event
import com.nexters.bandalart.feature.home.HomeScreen.State
import com.nexters.bandalart.feature.home.mapper.toUiModel
import com.nexters.bandalart.feature.home.model.CellType
import com.nexters.bandalart.feature.home.viewmodel.HomeUiEvent
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val _uiEvent = Channel<HomeUiEvent>()
    private val _state = MutableStateFlow<State?>(null)

    private val bandalartFlow = _state
        .map { it?.bandalartData }
        .filterNotNull()
        .distinctUntilChanged()

    @Composable
    override fun present(): State {
        val scope = rememberStableCoroutineScope()
        val state by rememberRetained {
            _state.value = State(
                eventSink = { event -> handleEvent(scope, event) },
            )
            _state
        }.collectAsRetainedState()

        val appVersion = remember {
            try {
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.tag("AppVersion").e(e, "Failed to get package info")
                "Unknown"
            }
        }

        LaunchedEffect(Unit) {
            _uiEvent.receiveAsFlow().collect { event ->
                when (event) {
                    is HomeUiEvent.NavigateToComplete -> {
                        navigateToComplete(
                            scope,
                            event.id,
                            event.title,
                            event.profileEmoji.ifEmpty { context.getString(R.string.home_default_emoji) },
                            event.bandalartChart,
                        )
                    }

                    is HomeUiEvent.ShowSnackbar -> {
//                        scope.launch {
//                            val job = launch {
//                                onShowSnackbar(event.message.asString(context))
//                            }
//                            delay(SnackbarDuration)
//                            job.cancel()
//                        }
                    }

                    is HomeUiEvent.ShowToast -> {
                        Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
                    }

                    is HomeUiEvent.SaveBandalart -> {
                        context.saveImageToGallery(event.bitmap)
                        Toast.makeText(context, context.getString(R.string.save_bandalart_image), Toast.LENGTH_SHORT).show()
                    }

                    is HomeUiEvent.ShareBandalart -> {
                        context.externalShareForBitmap(event.bitmap)
                    }

                    is HomeUiEvent.CaptureBandalart -> {
                        updateBandalartChartImageUri(context.bitmapToFileUri(event.bitmap).toString())
                    }

                    is HomeUiEvent.ShowAppVersion -> {
                        Toast.makeText(context, context.getString(R.string.app_version_info, appVersion), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            updateSkeletonState(true)
            getBandalartList(scope)
            observeBandalartCompletion(scope)
        }

        return state ?: error("State should not be null")
    }

    private fun handleEvent(scope: CoroutineScope, event: Event) {
        when (event) {
            is Event.OnListClick -> showBandalartListBottomSheet()
            is Event.OnSaveClick -> requestCapture()
            is Event.OnDeleteClick -> showBandalartDeleteDialog()
            is Event.OnEmojiSelected -> updateBandalartEmoji(
                scope,
                event.bandalartId,
                event.cellId,
                event.updateBandalartEmojiModel,
            )

            is Event.OnShareButtonClick -> requestShare()
            is Event.OnAddClick -> createBandalart(scope)
            is Event.OnMenuClick -> showDropDownMenu()
            is Event.OnDropDownMenuDismiss -> hideDropDownMenu()
            is Event.OnEmojiClick -> showEmojiBottomSheet()
            is Event.OnBandalartListItemClick -> {
                setRecentBandalartId(scope, event.key)
                getBandalart(scope, event.key)
                hideBottomSheet()
            }

            is Event.OnBandalartCellClick -> handleBandalartCellClick(
                scope,
                event.cellType,
                event.isMainCellTitleEmpty,
                event.cellData,
            )

            is Event.OnCloseButtonClick -> hideBottomSheet()
            is Event.OnAppTitleClick -> showAppVersion(scope)
            is Event.OnDismiss -> hideBottomSheet()
            is Event.OnCellTitleUpdate -> updateCellTitle(event.title, event.locale)
            is Event.OnEmojiSelect -> updateEmoji(event.emoji)
            is Event.OnColorSelect -> updateThemeColor(event.mainColor, event.subColor)
            is Event.OnDueDateSelect -> updateDueDate(event.date)
            is Event.OnDescriptionUpdate -> updateDescription(event.description)
            is Event.OnCompletionUpdate -> updateCompletion(event.isCompleted)
            is Event.OnDeleteBandalart -> deleteBandalart(scope, event.bandalartId)
            is Event.OnDeleteCell -> deleteCell(scope, event.cellId)
            is Event.OnCancelClick -> hideDialog()
            is Event.OnEmojiPickerClick -> expandEmojiPicker()
            is Event.OnDatePickerClick -> expandDatePicker()
            is Event.OnDeleteButtonClick -> showCellDeleteDialog()
            is Event.OnCompleteButtonClick -> {
                updateCell(
                    scope = scope,
                    bandalartId = event.bandalartId,
                    cellId = event.cellId,
                    cellType = event.cellType,
                )
            }

            else -> {}
        }
    }

    private fun observeBandalartCompletion(scope: CoroutineScope) {
        scope.launch {
            bandalartFlow.collect { bandalart ->
                if (bandalart.isCompleted && !bandalart.title.isNullOrEmpty()) {
                    val isBandalartCompleted = checkCompletedBandalartId(scope, bandalart.id)
                    if (isBandalartCompleted) {
                        delay(500L)
                        requestCapture()
                        delay(500L)
                        navigateToComplete(
                            scope,
                            bandalart.id,
                            bandalart.title,
                            bandalart.profileEmoji.orEmpty(),
                            _state.value?.bandalartChartUrl.orEmpty(),
                        )
                    }
                }
            }
        }
    }

    private fun navigateToComplete(
        scope: CoroutineScope,
        bandalartId: Long,
        title: String,
        profileEmoji: String,
        bandalartChart: String,
    ) {
        scope.launch {
            _uiEvent.send(
                HomeUiEvent.NavigateToComplete(
                    id = bandalartId,
                    title = title,
                    profileEmoji = profileEmoji,
                    bandalartChart = bandalartChart,
                ),
            )
        }
    }

    private fun getBandalartList(scope: CoroutineScope) {
        scope.launch {
            bandalartRepository.getBandalartList()
                .map { list -> list.map { it.toUiModel() } }
                .collect { bandalartList ->
                    _state.update { it?.copy(bandalartList = bandalartList.toImmutableList()) }

                    // 이전 반다라트 목록 상태 조회
                    val prevBandalartList = bandalartRepository.getPrevBandalartList()

                    // 새로 업데이트 된 상태와 이전 상태를 비교
                    val completedKeys = bandalartList.filter { bandalart ->
                        val prevBandalart = prevBandalartList.find { it.first == bandalart.id }
                        prevBandalart != null && !prevBandalart.second && bandalart.isCompleted
                    }.map { it.id }

                    // 이번에 목표를 달성한 반다라트가 존재하는 경우
                    if (completedKeys.isNotEmpty()) {
                        getBandalart(scope, completedKeys[0], isBandalartCompleted = true)
                        return@collect
                    }

                    // 데이터를 동기화
                    bandalartList.forEach { bandalart ->
                        bandalartRepository.upsertBandalartId(bandalart.id, bandalart.isCompleted)
                    }

                    // 반다라트 목록이 존재하지 않을 경우, 새로운 반다라트를 생성
                    if (bandalartList.isEmpty()) {
                        createBandalart(scope)
                        return@collect
                    } else {
                        // 반다라트 목록이 존재할 경우
                        // 가장 최근에 확인한 반다라트 표를 화면에 띄우는 경우
                        val recentBandalartId = getRecentBandalartId()
                        // 가장 최근에 확인한 반다라트 표가 존재 하는 경우
                        if (bandalartList.any { it.id == recentBandalartId }) {
                            getBandalart(scope, recentBandalartId)
                        }
                        // 가장 최근에 확인한 반다라트 표가 존재 하지 않을 경우
                        else {
                            // 목록에 가장 첫번째 표를 화면에 띄움
                            getBandalart(scope, bandalartList[0].id)
                        }
                    }
                }
        }
    }

    private fun getBandalart(
        scope: CoroutineScope,
        bandalartId: Long,
        isBandalartCompleted: Boolean = false,
    ) {
        scope.launch {
            updateSkeletonState(true)
            bandalartRepository.getBandalart(bandalartId).let { bandalart ->
                _state.update {
                    it?.copy(
                        bandalartData = bandalart.toUiModel(),
                        bottomSheet = null,
                        isBandalartCompleted = isBandalartCompleted,
                    )
                }
                getBandalartMainCell(scope, bandalartId)
            }
            updateSkeletonState(false)
        }
    }

    private fun getBandalartMainCell(
        scope: CoroutineScope,
        bandalartId: Long,
    ) {
        scope.launch {
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
            _state.update {
                it?.copy(
                    bandalartCellData = BandalartCellEntity(
                        id = mainCell?.id ?: 0L,
                        title = mainCell?.title,
                        description = mainCell?.description,
                        dueDate = mainCell?.dueDate,
                        isCompleted = mainCell?.isCompleted ?: false,
                        parentId = mainCell?.parentId,
                        children = children,
                    ),
                )
            }
            updateSkeletonState(false)
        }
    }

    private fun createBandalart(scope: CoroutineScope) {
        scope.launch {
            if ((_state.value?.bandalartList?.size?.plus(1) ?: 0) > 5) {
                _uiEvent.send(HomeUiEvent.ShowToast(UiText.StringResource(R.string.limit_create_bandalart)))
                return@launch
            }

            updateSkeletonState(true)
            bandalartRepository.createBandalart()?.let { bandalart ->
                hideBottomSheet()
                // 새로운 반다라트를 생성하면 화면에 생성된 반다라트 표를 보여주도록 id 를 전달
                getBandalart(scope, bandalart.id)
                // 새로운 반다라트의 키를 최근에 확인한 반다라트로 저장
                setRecentBandalartId(scope, bandalart.id)
                // 새로운 반다라트를 로컬에 저장
                upsertBandalartId(scope, bandalart.id)
                _uiEvent.send(HomeUiEvent.ShowSnackbar(UiText.StringResource(R.string.create_bandalart)))
            }
        }
    }

    fun saveBandalartImage(
        scope: CoroutineScope,
        bitmap: ImageBitmap,
    ) {
        scope.launch {
            _uiEvent.send(HomeUiEvent.SaveBandalart(bitmap))
            hideDropDownMenu()
        }
    }

    private fun deleteBandalart(
        scope: CoroutineScope,
        bandalartId: Long,
    ) {
        scope.launch {
            updateSkeletonState(true)
            bandalartRepository.deleteBandalart(bandalartId)
            hideModal()
            hideDropDownMenu()
            deleteBandalartId(scope, bandalartId)
            _uiEvent.send(HomeUiEvent.ShowSnackbar(UiText.StringResource(R.string.delete_bandalart)))
        }
    }

    private fun updateBandalartEmoji(
        scope: CoroutineScope,
        bandalartId: Long,
        cellId: Long,
        updateBandalartEmojiModel: UpdateBandalartEmojiEntity,
    ) {
        scope.launch {
            bandalartRepository.updateBandalartEmoji(bandalartId, cellId, updateBandalartEmojiModel)
            hideBottomSheet()
        }
    }

    private fun requestShare() {
        _state.update { it?.copy(isSharing = true) }
    }

    private fun requestCapture() {
        _state.update { it?.copy(isCapturing = true) }
    }

    private fun clearShareState() {
        _state.update { it?.copy(isSharing = false) }
    }

    private fun clearCaptureState() {
        _state.update { it?.copy(isCapturing = false) }
    }

    fun shareBandalart(
        scope: CoroutineScope,
        bitmap: ImageBitmap,
    ) {
        scope.launch {
            clearShareState()
            _uiEvent.send(HomeUiEvent.ShareBandalart(bitmap))
        }
    }

    fun captureBandalart(
        scope: CoroutineScope,
        bitmap: ImageBitmap,
    ) {
        scope.launch {
            clearCaptureState()
            _uiEvent.send(HomeUiEvent.CaptureBandalart(bitmap))
        }
    }

    private fun updateSkeletonState(flag: Boolean) {
        _state.update { it?.copy(isShowSkeleton = flag) }
    }

    private suspend fun getRecentBandalartId(): Long {
        return bandalartRepository.getRecentBandalartId()
    }

    private fun setRecentBandalartId(
        scope: CoroutineScope,
        bandalartId: Long,
    ) {
        scope.launch {
            bandalartRepository.setRecentBandalartId(bandalartId)
        }
    }

    private fun upsertBandalartId(
        scope: CoroutineScope,
        bandalartId: Long,
        isCompleted: Boolean = false,
    ) {
        scope.launch {
            bandalartRepository.upsertBandalartId(bandalartId, isCompleted)
        }
    }

    private suspend fun checkCompletedBandalartId(scope: CoroutineScope, bandalartId: Long): Boolean {
        return withContext(scope.coroutineContext) {
            bandalartRepository.checkCompletedBandalartId(bandalartId)
        }
    }

    private fun deleteBandalartId(scope: CoroutineScope, bandalartId: Long) {
        scope.launch {
            bandalartRepository.deleteCompletedBandalartId(bandalartId)
        }
    }

    private fun updateBandalartChartImageUri(url: String) {
        _state.update { it?.copy(bandalartChartUrl = url) }
    }

    // TODO 태스크셀이고 상위 서브셀이 비어있을 때(테스트셀이 자신의 부모를 알고있어야 구현 가능 함)
    private fun handleBandalartCellClick(
        scope: CoroutineScope,
        cellType: CellType,
        isMainCellTitleEmpty: Boolean,
        cellData: BandalartCellEntity,
    ) {
        when {
            cellType != CellType.MAIN && isMainCellTitleEmpty -> {
                scope.launch {
                    _uiEvent.send(HomeUiEvent.ShowToast(UiText.StringResource(R.string.please_input_main_goal)))
                }
            }

            else -> {
                _state.value?.bandalartData?.let { bandalartData ->
                    _state.update {
                        it?.copy(
                            clickedCellData = cellData,
                            clickedCellType = cellType,
                            bottomSheet = HomeScreen.BottomSheetState.Cell(
                                initialCellData = cellData,
                                cellData = cellData,
                                initialBandalartData = bandalartData,
                                bandalartData = bandalartData,
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun updateCellTitle(title: String, locale: Locale) {
        val maxLength = when (locale.language) {
            Locale.KOREAN.language, Locale.JAPAN.language -> 15
            else -> 24
        }

        _state.update {
            val currentBottomSheet = (it?.bottomSheet as? HomeScreen.BottomSheetState.Cell) ?: return@update it
            val validatedTitle = if (title.length > maxLength) {
                currentBottomSheet.cellData.title ?: ""
            } else title

            it.copy(
                bottomSheet = currentBottomSheet.copy(
                    cellData = currentBottomSheet.cellData.copy(title = validatedTitle),
                ),
            )
        }
    }

    private fun updateEmoji(emoji: String) {
        _state.update {
            val currentBottomSheet = (it?.bottomSheet as? HomeScreen.BottomSheetState.Cell) ?: return@update it
            it.copy(
                bottomSheet = currentBottomSheet.copy(
                    bandalartData = currentBottomSheet.bandalartData.copy(profileEmoji = emoji),
                ),
            )
        }
    }

    private fun updateThemeColor(mainColor: String, subColor: String) {
        _state.update {
            val currentSheet = it?.bottomSheet as? HomeScreen.BottomSheetState.Cell ?: return@update it
            it.copy(
                bottomSheet = currentSheet.copy(
                    bandalartData = currentSheet.bandalartData.copy(
                        mainColor = mainColor,
                        subColor = subColor,
                    ),
                ),
            )
        }
    }

    private fun updateDueDate(date: String) {
        _state.update {
            val currentBottomSheet = (it?.bottomSheet as? HomeScreen.BottomSheetState.Cell) ?: return@update it
            it.copy(
                bottomSheet = currentBottomSheet.copy(
                    cellData = currentBottomSheet.cellData.copy(dueDate = date),
                ),
            )
        }
    }

    private fun updateDescription(description: String) {
        _state.update {
            val currentBottomSheet = (it?.bottomSheet as? HomeScreen.BottomSheetState.Cell) ?: return@update it
            val validatedDescription = if (description.length > 1000) {
                currentBottomSheet.cellData.description
            } else {
                description
            }
            it.copy(
                bottomSheet = currentBottomSheet.copy(
                    cellData = currentBottomSheet.cellData.copy(description = validatedDescription),
                ),
            )
        }
    }

    private fun updateCompletion(isCompleted: Boolean) {
        _state.update {
            val currentBottomSheet = (it?.bottomSheet as? HomeScreen.BottomSheetState.Cell) ?: return@update it
            it.copy(
                bottomSheet = currentBottomSheet.copy(
                    cellData = currentBottomSheet.cellData.copy(isCompleted = isCompleted),
                ),
            )
        }
    }

    private fun deleteCell(scope: CoroutineScope, cellId: Long) {
        scope.launch {
            bandalartRepository.deleteBandalartCell(cellId)
            hideModal()
        }
    }

    private fun updateCell(
        scope: CoroutineScope,
        bandalartId: Long,
        cellId: Long,
        cellType: CellType,
    ) {
        val bottomSheetData = _state.value?.bottomSheet as? HomeScreen.BottomSheetState.Cell ?: return
        val cellData = bottomSheetData.cellData
        val bandalartData = bottomSheetData.bandalartData

        val trimmedTitle = cellData.title?.trim()
        val description = cellData.description
        val dueDate = cellData.dueDate?.ifEmpty { null }

        when (cellType) {
            CellType.MAIN -> {
                updateMainCell(
                    scope = scope,
                    bandalartId = bandalartId,
                    cellId = cellId,
                    updateBandalartMainCellModel = UpdateBandalartMainCellEntity(
                        title = trimmedTitle,
                        description = description,
                        dueDate = dueDate,
                        profileEmoji = bandalartData.profileEmoji,
                        mainColor = bandalartData.mainColor,
                        subColor = bandalartData.subColor,
                    ),
                )
            }

            CellType.SUB -> {
                updateSubCell(
                    scope = scope,
                    bandalartId = bandalartId,
                    cellId = cellId,
                    updateBandalartSubCellModel = UpdateBandalartSubCellEntity(
                        title = trimmedTitle,
                        description = description,
                        dueDate = dueDate,
                    ),
                )
            }

            else -> {
                updateTaskCell(
                    scope = scope,
                    bandalartId = bandalartId,
                    cellId = cellId,
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

    private fun updateMainCell(
        scope: CoroutineScope,
        bandalartId: Long,
        cellId: Long,
        updateBandalartMainCellModel: UpdateBandalartMainCellEntity,
    ) {
        scope.launch {
            bandalartRepository.updateBandalartMainCell(bandalartId, cellId, updateBandalartMainCellModel)
            bandalartRepository.getBandalart(bandalartId)
            hideBottomSheet()
        }
    }

    private fun updateSubCell(
        scope: CoroutineScope,
        bandalartId: Long,
        cellId: Long,
        updateBandalartSubCellModel: UpdateBandalartSubCellEntity,
    ) {
        scope.launch {
            bandalartRepository.updateBandalartSubCell(bandalartId, cellId, updateBandalartSubCellModel)
            hideBottomSheet()
        }
    }

    private fun updateTaskCell(
        scope: CoroutineScope,
        bandalartId: Long,
        cellId: Long,
        updateBandalartTaskCellModel: UpdateBandalartTaskCellEntity,
    ) {
        scope.launch {
            bandalartRepository.updateBandalartTaskCell(bandalartId, cellId, updateBandalartTaskCellModel)
            hideBottomSheet()
        }
    }

    private fun showDropDownMenu() {
        _state.update { it?.copy(isDropDownMenuOpened = true) }
    }

    private fun showEmojiBottomSheet() {
        _state.value?.bandalartData?.let { bandalartData ->
            _state.update {
                it?.copy(
                    bottomSheet = HomeScreen.BottomSheetState.Emoji(
                        bandalartId = bandalartData.id,
                        cellId = bandalartData.id,
                        currentEmoji = bandalartData.profileEmoji,
                    ),
                )
            }
        }
    }

    private fun showBandalartListBottomSheet() {
        _state.update {
            it?.copy(
                bottomSheet = HomeScreen.BottomSheetState.BandalartList(
                    bandalartList = it.bandalartList,
                    currentBandalartId = it.bandalartData?.id ?: return,
                ),
            )
        }
    }

    private fun showBandalartDeleteDialog() {
        _state.update { it?.copy(dialog = HomeScreen.DialogState.BandalartDelete) }
    }

    private fun showCellDeleteDialog() {
        _state.update {
            it?.copy(
                dialog = HomeScreen.DialogState.CellDelete(
                    cellType = it.clickedCellType,
                    cellTitle = (it.bottomSheet as? HomeScreen.BottomSheetState.Cell)?.cellData?.title,
                ),
            )
        }
    }

    private fun expandEmojiPicker() {
        _state.update {
            val currentBottomSheet = it?.bottomSheet as? HomeScreen.BottomSheetState.Cell ?: return@update it
            it.copy(
                bottomSheet = currentBottomSheet.copy(
                    isEmojiPickerOpened = true,
                ),
            )
        }
    }

    private fun expandDatePicker() {
        _state.update {
            val currentSheet = it?.bottomSheet as? HomeScreen.BottomSheetState.Cell ?: return@update it
            it.copy(
                bottomSheet = currentSheet.copy(
                    isDatePickerOpened = true,
                ),
            )
        }
    }

    private fun hideDropDownMenu() {
        _state.update { it?.copy(isDropDownMenuOpened = false) }
    }

    private fun hideModal() {
        hideDialog()
        hideBottomSheet()
    }

    private fun hideDialog() {
        _state.update { it?.copy(dialog = null) }
    }

    private fun hideBottomSheet() {
        _state.update { it?.copy(bottomSheet = null) }
    }

    private fun showAppVersion(scope: CoroutineScope) {
        scope.launch {
            _uiEvent.send(HomeUiEvent.ShowAppVersion)
        }
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
