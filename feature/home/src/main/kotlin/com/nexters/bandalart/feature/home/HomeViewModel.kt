package com.nexters.bandalart.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.core.common.UiText
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.feature.home.mapper.toEntity
import com.nexters.bandalart.feature.home.mapper.toUiModel
import com.nexters.bandalart.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.feature.home.model.BandalartDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.nexters.bandalart.core.ui.R

/**
 * HomeUiState
 *
 * @param bandalartList 반다라트 목록
 * @param bandalartDetailData 반다라트 상세 데이터, 서버와의 통신을 성공하면 not null
 * @param bandalartCellData 반다라트 표의 데이터, 서버와의 통신을 성공하면 not null
 * @param isBandalartDeleted 표가 삭제됨
 * @param isDropDownMenuOpened 드롭 다운 메뉴가 열림
 * @param isBandalartDeleteAlertDialogOpened 반다라트 표 삭제 다이얼로그가 열림
 * @param isBandalartListBottomSheetOpened 반다라트 목록 바텀시트가 열림
 * @param isCellBottomSheetOpened 반다라트 셀 바텀시트가 열림
 * @param isEmojiBottomSheetOpened 반다라트 이모지 바텀시트가 열림
 * @param isBottomSheetDataChanged 바텀시트의 데이터가 변경됨
 * @param isBottomSheetMainCellChanged 바텀시트의 변경된 데이터가 메인 셀임
 * @param isBandalartCompleted 반다라트 목표를 달성함
 * @param isLoading 로딩 상태
 * @param isShowSkeleton 표의 첫 로딩을 보여주는 스켈레톤 이미지
 */

data class HomeUiState(
    val bandalartList: ImmutableList<BandalartDetailUiModel> = persistentListOf(),
    val bandalartDetailData: BandalartDetailUiModel? = null,
    val bandalartCellData: BandalartCellUiModel? = null,
    val isBandalartDeleted: Boolean = false,
    val isDropDownMenuOpened: Boolean = false,
    val isBandalartDeleteAlertDialogOpened: Boolean = false,
    val isBandalartListBottomSheetOpened: Boolean = false,
    val isCellBottomSheetOpened: Boolean = false,
    val isEmojiBottomSheetOpened: Boolean = false,
    val isBottomSheetDataChanged: Boolean = false,
    val isBottomSheetMainCellChanged: Boolean = false,
    val shareUrl: String = "",
    val isBandalartCompleted: Boolean = false,
    val isLoading: Boolean = false,
    val isShowSkeleton: Boolean = false,
)

sealed interface HomeUiEvent {
    data class NavigateToComplete(
        val id: Long,
        val title: String,
        val profileEmoji: String,
    ) : HomeUiEvent

    data class ShowSnackbar(val message: UiText) : HomeUiEvent
    data class ShowToast(val message: UiText) : HomeUiEvent
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bandalartRepository: BandalartRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<HomeUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        _uiState.update { it.copy(isShowSkeleton = true) }
    }

    fun getBandalartList(bandalartId: Long? = null) {
        viewModelScope.launch {
            val bandalartList = bandalartRepository.getBandalartList().map { it.toUiModel() }
            _uiState.update {
                it.copy(bandalartList = bandalartList.toImmutableList())
            }

            // 이전 반다라트 목록 상태 조회
            val prevBandalartList = bandalartRepository.getPrevBandalartList()

            // 새로 업데이트 된 상태와 이전 상태를 비교
            val completedKeys = bandalartList.filter { bandalart ->
                val prevBandalart = prevBandalartList.find { it.first == bandalart.id }
                prevBandalart != null && !prevBandalart.second && bandalart.isCompleted
            }.map { it.id }

            // 이번에 목표를 달성한 반다라트가 존재하는 경우
            if (completedKeys.isNotEmpty()) {
                getBandalartDetail(completedKeys[0], isBandalartCompleted = true)
                return@launch
            }

            // 서버의 데이터와 로컬의 데이터를 동기화
            bandalartList.forEach { bandalart ->
                bandalartRepository.upsertBandalartId(bandalart.id, bandalart.isCompleted)
            }

            // 생성한 반다라트 표를 화면에 띄우는 경우
            if (bandalartId != null) {
                _uiState.update { it.copy(isShowSkeleton = true) }
                getBandalartDetail(bandalartId)
                return@launch
            }
            // 반다라트 목록이 존재하지 않을 경우, 새로운 반다라트를 생성
            if (bandalartList.isEmpty()) {
                createBandalart()
                return@launch
            } else {
                // 반다라트 목록이 존재할 경우
                // 가장 최근에 확인한 반다라트 표를 화면에 띄우는 경우
                val recentBandalartId = getRecentBandalartId()
                // 가장 최근에 확인한 반다라트 표가 존재 하는 경우
                if (bandalartList.any { it.id == recentBandalartId }) {
                    getBandalartDetail(recentBandalartId)
                }
                // 가장 최근에 확인한 반다라트 표가 존재 하지 않을 경우
                else {
                    _uiState.update { it.copy(isShowSkeleton = true) }
                    // 목록에 가장 첫번째 표를 화면에 띄움
                    getBandalartDetail(bandalartList[0].id)
                }
            }
        }
    }

    fun getBandalartDetail(bandalartId: Long, isBandalartCompleted: Boolean = false) {
        viewModelScope.launch {
            bandalartRepository.getBandalartDetail(bandalartId)?.let { detail ->
                _uiState.update {
                    it.copy(
                        bandalartDetailData = detail.toUiModel(),
                        isBandalartListBottomSheetOpened = false,
                        isBandalartCompleted = isBandalartCompleted,
                    )
                }
                getBandalartMainCell(bandalartId)
            }
        }
        _uiState.update { it.copy(isShowSkeleton = false) }
    }

    private fun getBandalartMainCell(bandalartId: Long) {
        viewModelScope.launch {
            bandalartRepository.getBandalartMainCell(bandalartId)?.let { mainCell ->
                _uiState.update {
                    it.copy(
                        bandalartCellData = mainCell.toUiModel(),
                        isShowSkeleton = false,
                    )
                }
                bottomSheetDataChanged(flag = false)
            }
        }
    }

    fun createBandalart() {
        viewModelScope.launch {
            if (_uiState.value.bandalartList.size + 1 > 5) {
                _uiEvent.send(HomeUiEvent.ShowToast(UiText.StringResource(R.string.limit_create_bandalart)))
                return@launch
            }

            _uiState.update { it.copy(isShowSkeleton = true) }

            bandalartRepository.createBandalart()?.let { bandalart ->
                _uiState.update {
                    it.copy(isBandalartListBottomSheetOpened = false)
                }
                // 새로운 반다라트를 생성하면 화면에 생성된 반다라트 표를 보여주도록 id 를 전달
                getBandalartList(bandalart.id)
                // 새로운 반다라트의 키를 최근에 확인한 반다라트로 저장
                setRecentBandalartId(bandalart.id!!)
                // 새로운 반다라트를 로컬에 저장
                upsertBandalartId(bandalart.id!!)
                _uiEvent.send(HomeUiEvent.ShowSnackbar(UiText.StringResource(R.string.create_bandalart)))
            }
        }
    }

    fun deleteBandalart(bandalartId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isShowSkeleton = true) }

            bandalartRepository.deleteBandalart(bandalartId)
            _uiState.update {
                it.copy(isBandalartDeleted = true)
            }
            openBandalartDeleteAlertDialog(false)
            getBandalartList()
            deleteBandalartId(bandalartId)
            _uiEvent.send(HomeUiEvent.ShowSnackbar(UiText.StringResource(R.string.delete_bandalart)))
        }
    }

    fun updateBandalartEmoji(
        bandalartId: Long,
        cellId: Long,
        updateBandalartEmojiModel: com.nexters.bandalart.feature.home.model.UpdateBandalartEmojiModel,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartEmoji(bandalartId, cellId, updateBandalartEmojiModel.toEntity())
        }
    }

    fun shareBandalart() {}

    fun openDropDownMenu(flag: Boolean) {
        _uiState.update { it.copy(isDropDownMenuOpened = flag) }
    }

    fun openBandalartDeleteAlertDialog(flag: Boolean) {
        _uiState.update { it.copy(isBandalartDeleteAlertDialogOpened = flag) }
    }

    fun openEmojiBottomSheet(flag: Boolean) {
        _uiState.update { it.copy(isEmojiBottomSheetOpened = flag) }
    }

    fun openCellBottomSheet(flag: Boolean) {
        _uiState.update { it.copy(isCellBottomSheetOpened = flag) }
    }

    fun bottomSheetDataChanged(flag: Boolean) {
        _uiState.update { it.copy(isBottomSheetDataChanged = flag) }
    }

    fun showSkeletonChanged(flag: Boolean) {
        _uiState.update { it.copy(isShowSkeleton = flag) }
    }

    fun openBandalartListBottomSheet(flag: Boolean) {
        _uiState.update { it.copy(isBandalartListBottomSheetOpened = flag) }
    }

    private suspend fun getRecentBandalartId(): Long {
        return bandalartRepository.getRecentBandalartId()
    }

    fun setRecentBandalartId(bandalartId: Long) {
        viewModelScope.launch {
            bandalartRepository.setRecentBandalartId(bandalartId)
        }
    }

    private fun upsertBandalartId(bandalartId: Long, isCompleted: Boolean = false) {
        viewModelScope.launch {
            bandalartRepository.upsertBandalartId(bandalartId, isCompleted)
        }
    }

    suspend fun checkCompletedBandalartId(bandalartId: Long): Boolean {
        return withContext(viewModelScope.coroutineContext) {
            bandalartRepository.checkCompletedBandalartId(bandalartId)
        }
    }

    private fun deleteBandalartId(bandalartId: Long) {
        viewModelScope.launch {
            bandalartRepository.deleteBandalartId(bandalartId)
        }
    }

    fun navigateToComplete() {
        viewModelScope.launch {
            uiState.value.bandalartDetailData?.let { detail ->
                detail.title?.let { title ->
                    _uiEvent.send(
                        HomeUiEvent.NavigateToComplete(
                            id = detail.id,
                            title = title,
                            profileEmoji = detail.profileEmoji.orEmpty(),
                        ),
                    )
                }
            }
        }
    }
}
