package com.nexters.bandalart.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.domain.usecase.bandalart.CheckCompletedBandalartKeyUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.CreateBandalartUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.DeleteBandalartKeyUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.DeleteBandalartUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetBandalartDetailUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetBandalartListUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetBandalartMainCellUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetPrevBandalartListUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetRecentBandalartKeyUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.SetRecentBandalartKeyUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.ShareBandalartUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.UpdateBandalartEmojiUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.UpsertBandalartKeyUseCase
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.UiText
import com.nexters.bandalart.android.feature.home.mapper.toEntity
import com.nexters.bandalart.android.feature.home.mapper.toUiModel
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartEmojiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * HomeUiState
 *
 * @param bandalartList 반다라트 목록
 * @param bandalartDetailData 반다라트 상세 데이터, 서버와의 통신을 성공하면 not null
 * @param bandalartCellData 반다라트 표의 데이터, 서버와의 통신을 성공하면 not null
 * @param isBandalartDeleted 표가 삭제됨
 * @param isDropDownMenuOpened 드롭다운메뉴가 열림
 * @param isBandalartDeleteAlertDialogOpened 반다라트 표 삭제 다이얼로그가 열림
 * @param isNetworkErrorAlertDialogOpened 통신 시 네트워크 문제 발생
 * @param isBandalartListBottomSheetOpened 반다라트 목록 바텀시트가 열림
 * @param isCellBottomSheetOpened 반다라트 셀 바텀시트가 열림
 * @param isEmojiBottomSheetOpened 반다라트 이모지 바텀시트가 열림
 * @param isBottomSheetDataChanged 바텀시트의 데이터가 변경됨
 * @param isBottomSheetMainCellChanged 바텀시트의 변경된 데이터가 메인 셀임
 * @param isBandalartCompleted 반다라트 목표를 달성함
 * @param isLoading 서버와의 통신 중 로딩 상태
 * @param isShowSkeleton 표의 첫 로딩을 보여주는 스켈레톤 이미지
 * @param shareUrl 공유 링크
 * @param isNetworking 중복 통신 호출 방지를 위해 통신 중임을 알림
 */

data class HomeUiState(
  val bandalartList: ImmutableList<BandalartDetailUiModel> = persistentListOf(),
  val bandalartDetailData: BandalartDetailUiModel = BandalartDetailUiModel(),
  val bandalartCellData: BandalartCellUiModel? = null,
  val isBandalartDeleted: Boolean = false,
  val isDropDownMenuOpened: Boolean = false,
  val isBandalartDeleteAlertDialogOpened: Boolean = false,
  val isNetworkErrorAlertDialogOpened: Boolean = false,
  val isBandalartListBottomSheetOpened: Boolean = false,
  val isCellBottomSheetOpened: Boolean = false,
  val isEmojiBottomSheetOpened: Boolean = false,
  val isBottomSheetDataChanged: Boolean = false,
  val isBottomSheetMainCellChanged: Boolean = false,
  val shareUrl: String = "",
  val isBandalartCompleted: Boolean = false,
  val isShowSkeleton: Boolean = false,
  val isLoading: Boolean = false,
  val isNetworking: Boolean = false,
)

sealed interface HomeUiEvent {
  data class NavigateToComplete(
    val key: String,
    val title: String,
    val profileEmoji: String,
  ) : HomeUiEvent

  data class ShowSnackbar(val message: UiText) : HomeUiEvent
  data class ShowToast(val message: UiText) : HomeUiEvent
}

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getBandalartListUseCase: GetBandalartListUseCase,
  private val getBandalartDetailUseCase: GetBandalartDetailUseCase,
  private val getBandalartMainCellUseCase: GetBandalartMainCellUseCase,
  private val createBandalartUseCase: CreateBandalartUseCase,
  private val deleteBandalartUseCase: DeleteBandalartUseCase,
  private val updateBandalartEmojiUseCase: UpdateBandalartEmojiUseCase,
  private val getRecentBandalartKeyUseCase: GetRecentBandalartKeyUseCase,
  private val setRecentBandalartKeyUseCase: SetRecentBandalartKeyUseCase,
  private val shareBandalartUseCase: ShareBandalartUseCase,
  private val upsertBandalartKeyUseCase: UpsertBandalartKeyUseCase,
  private val checkCompletedBandalartKeyUseCase: CheckCompletedBandalartKeyUseCase,
  private val deleteBandalartKeyUseCase: DeleteBandalartKeyUseCase,
  private val getPrevBandalartListUseCase: GetPrevBandalartListUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(HomeUiState())
  val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

  private val _eventFlow = MutableSharedFlow<HomeUiEvent>()
  val eventFlow: SharedFlow<HomeUiEvent> = _eventFlow.asSharedFlow()

  init {
    _uiState.update { it.copy(isShowSkeleton = true) }
  }

  fun getBandalartList(bandalartKey: String? = null) {
    viewModelScope.launch {
      val result = getBandalartListUseCase()
      when {
        result.isSuccess && result.getOrNull() != null -> {
          val bandalartList = result.getOrNull()!!.map { it.toUiModel() }
          _uiState.update {
            it.copy(bandalartList = bandalartList.toImmutableList())
          }
          // 이전 반다라트 목록 상태 조회
          val prevBandalartList = getPrevBandalartListUseCase()

          // 새로 업데이트 된 상태와 이전 상태를 비교
          val completedKeys = bandalartList.filter { bandalart ->
            val prevBandalart = prevBandalartList.find { it.first == bandalart.key }
            prevBandalart != null && !prevBandalart.second && bandalart.isCompleted
          }.map { it.key }

          // 이번에 목표를 달성한 반다라트가 존재하는 경우
          if (completedKeys.isNotEmpty()) {
            getBandalartDetail(completedKeys[0], isBandalartCompleted = true)
            return@launch
          }

          // 서버의 데이터와 로컬의 데이터를 동기화
          bandalartList.forEach { bandalart ->
            upsertBandalartKeyUseCase(bandalart.key, bandalart.isCompleted)
          }

          // 생성한 반다라트 표를 화면에 띄우는 경우
          if (bandalartKey != null) {
            _uiState.update { it.copy(isShowSkeleton = true) }
            getBandalartDetail(bandalartKey)
            return@launch
          }
          // 반다라트 목록이 존재하지 않을 경우, 새로운 반다라트를 생성
          if (bandalartList.isEmpty()) {
            _uiState.update { it.copy(isNetworking = false) }
            createBandalart()
            return@launch
          }
          // 반다라트 목록이 존재할 경우
          else {
            // 가장 최근에 확인한 반다라트 표를 화면에 띄우는 경우
            val recentBandalartkey = getRecentBandalartKey()
            // 가장 최근에 확인한 반다라트 표가 존재 하는 경우
            if (bandalartList.any { it.key == recentBandalartkey }) {
              getBandalartDetail(recentBandalartkey)
            }
            // 가장 최근에 확인한 반다라트 표가 존재 하지 않을 경우
            else {
              _uiState.update { it.copy(isShowSkeleton = true) }
              // 목록에 가장 첫번째 표를 화면에 띄움
              getBandalartDetail(bandalartList[0].key)
            }
          }
        }

        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }

        result.isFailure -> {
          _uiState.update {
            it.copy(
              isLoading = false,
              isShowSkeleton = false,
              isNetworkErrorAlertDialogOpened = true,
            )
          }
        }
      }
      _uiState.update { it.copy(isNetworking = false) }
    }
  }

  fun getBandalartDetail(bandalartKey: String, isBandalartCompleted: Boolean = false) {
    viewModelScope.launch {
      val result = getBandalartDetailUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          val bandalartDetailData = result.getOrNull()!!.toUiModel()
          _uiState.update {
            it.copy(
              bandalartDetailData = bandalartDetailData,
              isBandalartListBottomSheetOpened = false,
              isBandalartCompleted = isBandalartCompleted,
            )
          }
          getBandalartMainCell(bandalartKey)
        }

        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }

        result.isFailure -> {
          _uiState.update {
            it.copy(
              isLoading = false,
              isShowSkeleton = false,
              isNetworkErrorAlertDialogOpened = true,
            )
          }
        }
      }
      _uiState.update { it.copy(isNetworking = false) }
    }
  }

  private fun getBandalartMainCell(bandalartKey: String) {
    viewModelScope.launch {
      val result = getBandalartMainCellUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.update {
            it.copy(
              isShowSkeleton = false,
              bandalartCellData = result.getOrNull()!!.toUiModel(),
            )
          }
          bottomSheetDataChanged(flag = false)
          openNetworkErrorAlertDialog(false)
        }

        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }

        result.isFailure -> {
          _uiState.update {
            it.copy(
              isNetworkErrorAlertDialogOpened = true,
              isShowSkeleton = false,
            )
          }
        }
      }
      _uiState.update {
        it.copy(
          isNetworking = false,
          isLoading = false,
        )
      }
    }
  }

  fun createBandalart() {
    if (_uiState.value.isNetworking)
      return

    _uiState.update { it.copy(isNetworking = true) }
    viewModelScope.launch {
      if (_uiState.value.bandalartList.size + 1 > 5) {
        _eventFlow.emit(HomeUiEvent.ShowToast(UiText.StringResource(R.string.limit_create_bandalart)))
        return@launch
      }
      _uiState.update { it.copy(isShowSkeleton = true) }
      val result = createBandalartUseCase()
      when {
        result.isSuccess && result.getOrNull() != null -> {
          val bandalart = result.getOrNull()!!
          _uiState.update {
            it.copy(isBandalartListBottomSheetOpened = false)
          }
          // 새로운 반다라트를 생성하면 화면에 생성된 반다라트 표를 보여주도록 key 를 전달
          getBandalartList(bandalart.key)
          // 새로운 반다라트의 키를 최근에 확인한 반다라트로 저장
          setRecentBandalartKey(bandalart.key)
          // 새로운 반다라트를 로컬에 저장
          upsertBandalartKey(bandalart.key)
          _eventFlow.emit(HomeUiEvent.ShowSnackbar(UiText.StringResource(R.string.create_bandalart)))
        }

        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }

        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.update {
            it.copy(
              isLoading = false,
              isShowSkeleton = false,
            )
          }
          _eventFlow.emit(HomeUiEvent.ShowToast(UiText.DirectString(exception.message.toString())))
        }
      }
      _uiState.update { it.copy(isNetworking = false) }
    }
  }

  fun deleteBandalart(bandalartKey: String) {
    if (_uiState.value.isNetworking)
      return

    _uiState.update { it.copy(isNetworking = true) }
    viewModelScope.launch {
      _uiState.update { it.copy(isShowSkeleton = true) }
      val result = deleteBandalartUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.update {
            it.copy(isBandalartDeleted = true)
          }
          openBandalartDeleteAlertDialog(false)
          getBandalartList()
          deleteBandalartKey(bandalartKey)
          _eventFlow.emit(HomeUiEvent.ShowSnackbar(UiText.StringResource(R.string.delete_bandalart)))
        }

        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }

        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.update {
            it.copy(
              isLoading = false,
              isShowSkeleton = false,
              isBandalartDeleted = false,
            )
          }
          _eventFlow.emit(HomeUiEvent.ShowToast(UiText.DirectString(exception.message.toString())))
        }
      }
      _uiState.update { it.copy(isNetworking = false) }
    }
  }

  fun updateBandalartEmoji(
    bandalartKey: String,
    cellKey: String,
    updateBandalartEmojiModel: UpdateBandalartEmojiModel,
  ) {
    if (_uiState.value.isNetworking)
      return

    _uiState.update { it.copy(isNetworking = true) }
    viewModelScope.launch {
      // _uiState.update { it.copy(isLoading = true) }
      val result = updateBandalartEmojiUseCase(bandalartKey, cellKey, updateBandalartEmojiModel.toEntity())
      when {
        result.isSuccess && result.getOrNull() != null -> {}
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }

        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.update {
            it.copy(
              isLoading = false,
              isShowSkeleton = false,
            )
          }
          _eventFlow.emit(HomeUiEvent.ShowToast(UiText.DirectString(exception.message.toString())))
        }
      }
      _uiState.update { it.copy(isNetworking = false) }
    }
  }

  fun shareBandalart(bandalartKey: String) {
    if (uiState.value.isNetworking || _uiState.value.shareUrl.isNotEmpty())
      return

    _uiState.update { it.copy(isNetworking = true) }
    viewModelScope.launch {
      val result = shareBandalartUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.update {
            it.copy(shareUrl = result.getOrNull()!!.shareUrl)
          }
        }

        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }

        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _eventFlow.emit(HomeUiEvent.ShowToast(UiText.DirectString(exception.message.toString())))
        }
      }
      _uiState.update { it.copy(isNetworking = false) }
    }
  }

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

//  fun loadingChanged(flag: Boolean) {
//    _uiState.update { it.copy(isLoading = flag) }
//  }

  fun showSkeletonChanged(flag: Boolean) {
    _uiState.update { it.copy(isShowSkeleton = flag) }
  }

  fun openNetworkErrorAlertDialog(flag: Boolean) {
    _uiState.update { it.copy(isNetworkErrorAlertDialogOpened = flag) }
  }

  fun openBandalartListBottomSheet(flag: Boolean) {
    _uiState.update { it.copy(isBandalartListBottomSheetOpened = flag) }
  }

  private suspend fun getRecentBandalartKey(): String {
    return viewModelScope.async {
      getRecentBandalartKeyUseCase()
    }.await()
  }

  fun setRecentBandalartKey(bandalartKey: String) {
    viewModelScope.launch {
      setRecentBandalartKeyUseCase(bandalartKey)
    }
  }

  fun initShareUrl() {
    _uiState.update { it.copy(shareUrl = "") }
  }

  private fun upsertBandalartKey(bandalartKey: String, isCompleted: Boolean = false) {
    viewModelScope.launch {
      upsertBandalartKeyUseCase(bandalartKey, isCompleted)
    }
  }

  suspend fun checkCompletedBandalartKey(bandalartKey: String): Boolean {
    return viewModelScope.async {
      checkCompletedBandalartKeyUseCase(bandalartKey)
    }.await()
  }

  private suspend fun deleteBandalartKey(bandalartKey: String) {
    viewModelScope.launch {
      deleteBandalartKeyUseCase(bandalartKey)
    }
  }

  fun navigateToComplete() {
    viewModelScope.launch {
      _eventFlow.emit(
        HomeUiEvent.NavigateToComplete(
          key = uiState.value.bandalartDetailData.key,
          title = uiState.value.bandalartDetailData.title!!,
          profileEmoji = uiState.value.bandalartDetailData.profileEmoji ?: "",
        ),
      )
    }
  }
}
