package com.nexters.bandalart.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.domain.usecase.bandalart.CheckCompletedBandalartKeyUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.CreateBandalartUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.DeleteBandalartUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetBandalartDetailUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetBandalartListUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetBandalartMainCellUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetRecentBandalartKeyUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.InsertCompletedBandalartKeyUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.SetRecentBandalartKeyUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.ShareBandalartUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.UpdateBandalartEmojiUseCase
import com.nexters.bandalart.android.feature.home.mapper.toEntity
import com.nexters.bandalart.android.feature.home.mapper.toUiModel
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartEmojiModel
import com.nexters.bandalart.android.feature.home.util.StringResource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

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
 * @param isLoading 서버와의 통신 중 로딩 상태
 * @param isShowSkeleton 표의 첫 로딩을 보여주는 스켈레톤 이미지
 * @param shareUrl 공유 링크
 * @param error 서버와의 통신을 실패
 */

data class HomeUiState(
  val bandalartList: List<BandalartDetailUiModel> = emptyList(),
  val bandalartDetailData: BandalartDetailUiModel? = null,
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
  val isBandalartFirstCompleted: Boolean = false,
  val isShowSkeleton: Boolean = false,
  val isLoading: Boolean = false,
  val error: Throwable? = null,
)

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
  private val insertCompletedBandalartKeyUseCase: InsertCompletedBandalartKeyUseCase,
  private val checkCompletedBandalartKeyUseCase: CheckCompletedBandalartKeyUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(HomeUiState())
  val uiState: StateFlow<HomeUiState> = this._uiState.asStateFlow()

  private val _snackbarMessage = Channel<StringResource>()
  val snackbarMessage = _snackbarMessage.receiveAsFlow()

  private val _toastMessage = Channel<StringResource>()
  val toastMessage = _toastMessage.receiveAsFlow()

  private val _logMessage = Channel<StringResource>()
  val logMessage = _logMessage.receiveAsFlow()

  init {
    _uiState.value = _uiState.value.copy(
      isShowSkeleton = true,
    )
  }

  fun getBandalartList(bandalartKey: String? = null) {
    viewModelScope.launch {
      val result = getBandalartListUseCase()
      when {
        result.isSuccess && result.getOrNull() != null -> {
          val bandalartList = result.getOrNull()!!.map { it.toUiModel() }
          _uiState.value = _uiState.value.copy(
            bandalartList = bandalartList,
            error = null,
          )
          // 생성한 반다라트 표를 화면에 띄우는 경우
          if (bandalartKey != null) {
            _uiState.value = _uiState.value.copy(isShowSkeleton = true)
            getBandalartDetail(bandalartKey)
            return@launch
          }

          // 반다라트 목록이 존재하지 않을 경우, 새로운 반다라트를 생성
          if (bandalartList.isEmpty()) {
            createBandalart()
          }
          // 반다라트 목록이 존재할 경우
          else {
            // 가장 최근에 확인한 반다라트 표를 화면에 띄우는 경우
            val recentBandalartkey = getRecentBandalartKey()
            // 가장 최근에 확인한 반다라트 표가 존재하는 경우
            if (bandalartList.any { it.key == recentBandalartkey }) {
              getBandalartDetail(recentBandalartkey)
            }
            // 가장 최근에 확인한 반다라트 표가 존재하지 않을 경우
            else {
              _uiState.value = _uiState.value.copy(isShowSkeleton = true)
              // 목록에 가장 첫번째 표를 화면에 띄움
              getBandalartDetail(bandalartList[0].key)
            }
            bandalartList.filter { it.isCompleted }.forEach {
              insertCompletedBandalartKey(it.key)
            }
          }
        }
        // TODO 해당 케이스의 대한 처리 유무 결정
        result.isSuccess && result.getOrNull() == null -> {
          _logMessage.send(StringResource.StringResourceText(R.string.data_validation_text))
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            isShowSkeleton = false,
            isNetworkErrorAlertDialogOpened = true,
            error = exception,
          )
          _logMessage.send(StringResource.ViewModelStringViewModel(exception.message.toString()))
        }
      }
    }
  }

  fun getBandalartDetail(bandalartKey: String) {
    viewModelScope.launch {
      val result = getBandalartDetailUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          val bandalartDetailData = result.getOrNull()!!.toUiModel()
          _uiState.value = _uiState.value.copy(
            bandalartDetailData = bandalartDetailData,
            isBandalartListBottomSheetOpened = false,
            error = null,
          )
          getBandalartMainCell(bandalartKey)
        }
        result.isSuccess && result.getOrNull() == null -> {
          _logMessage.send(StringResource.StringResourceText(R.string.data_validation_text))
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            isShowSkeleton = false,
            bandalartCellData = null,
            isNetworkErrorAlertDialogOpened = true,
            error = exception,
          )
          _logMessage.send(StringResource.ViewModelStringViewModel(exception.message.toString()))
        }
      }
    }
  }

  private fun getBandalartMainCell(bandalartKey: String) {
    viewModelScope.launch {
      val result = getBandalartMainCellUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            isShowSkeleton = false,
            bandalartCellData = result.getOrNull()!!.toUiModel(),
            error = null,
          )
          bottomSheetDataChanged(isBottomSheetDataChangedState = false)
          openNetworkErrorAlertDialog(false)
        }
        result.isSuccess && result.getOrNull() == null -> {
          _logMessage.send(StringResource.StringResourceText(R.string.data_validation_text))
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            bandalartCellData = null,
            isNetworkErrorAlertDialogOpened = true,
            isLoading = false,
            isShowSkeleton = false,
            error = exception,
          )
          _logMessage.send(StringResource.ViewModelStringViewModel(exception.message.toString()))
        }
      }
    }
  }

  fun createBandalart() {
    viewModelScope.launch {
      if (_uiState.value.bandalartList.size + 1 > 5) {
        _toastMessage.send(StringResource.StringResourceText(R.string.limit_create_bandalart_text))
        return@launch
      }
      _uiState.value = _uiState.value.copy(isShowSkeleton = true)
      val result = createBandalartUseCase()
      when {
        result.isSuccess && result.getOrNull() != null -> {
          val bandalart = result.getOrNull()!!
          _uiState.value = _uiState.value.copy(
            isBandalartListBottomSheetOpened = false,
            error = null,
          )
          // 새로운 반다라트를 생성하면 화면에 생성된 반다라트 표를 보여주도록 key 를 전달
          getBandalartList(bandalart.key)
          // TODO 표가 뒤집히는 애니메이션 구현
          _snackbarMessage.send(StringResource.StringResourceText(R.string.create_bandalart_text))
        }
        result.isSuccess && result.getOrNull() == null -> {
          _logMessage.send(StringResource.StringResourceText(R.string.data_validation_text))
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            isShowSkeleton = false,
            error = exception,
          )
          _logMessage.send(StringResource.ViewModelStringViewModel(exception.message.toString()))
          _snackbarMessage.send(StringResource.ViewModelStringViewModel(exception.message.toString()))
        }
      }
    }
  }

  fun deleteBandalart(bandalartKey: String) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isShowSkeleton = true)
      val result = deleteBandalartUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.value = _uiState.value.copy(
            isBandalartDeleted = true,
            error = null,
          )
          openBandalartDeleteAlertDialog(false)
          getBandalartList()
          _snackbarMessage.send(StringResource.StringResourceText(R.string.delete_bandalart_text))
        }
        result.isSuccess && result.getOrNull() == null -> {
          _logMessage.send(StringResource.StringResourceText(R.string.data_validation_text))
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            isShowSkeleton = false,
            isBandalartDeleted = false,
            error = exception,
          )
          _logMessage.send(StringResource.ViewModelStringViewModel(exception.message.toString()))
          _snackbarMessage.send(StringResource.ViewModelStringViewModel(exception.message.toString()))
        }
      }
    }
  }

  fun updateBandalartEmoji(
    bandalartKey: String,
    cellKey: String,
    updateBandalartEmojiModel: UpdateBandalartEmojiModel,
  ) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true)
      val result = updateBandalartEmojiUseCase(bandalartKey, cellKey, updateBandalartEmojiModel.toEntity())
      when {
        result.isSuccess && result.getOrNull() != null -> {
          getBandalartList(bandalartKey)
        }
        result.isSuccess && result.getOrNull() == null -> {
          _logMessage.send(StringResource.StringResourceText(R.string.data_validation_text))
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            isShowSkeleton = false,
            error = exception,
          )
          _logMessage.send(StringResource.ViewModelStringViewModel(exception.message.toString()))
          _snackbarMessage.send(StringResource.ViewModelStringViewModel(exception.message.toString()))
        }
      }
    }
  }

  fun shareBandalart(bandalartKey: String) {
    viewModelScope.launch {
      val result = shareBandalartUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.value = _uiState.value.copy(
            shareUrl = result.getOrNull()!!.shareUrl,
            error = null,
          )
        }
        result.isSuccess && result.getOrNull() == null -> {
          _logMessage.send(StringResource.StringResourceText(R.string.data_validation_text))
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(error = exception)
          _logMessage.send(StringResource.ViewModelStringViewModel(exception.message.toString()))
          _snackbarMessage.send(StringResource.ViewModelStringViewModel(exception.message.toString()))
        }
      }
    }
  }

  fun openDropDownMenu(state: Boolean) {
    _uiState.value = _uiState.value.copy(
      isDropDownMenuOpened = state,
    )
  }

  fun openBandalartDeleteAlertDialog(state: Boolean) {
    _uiState.value = _uiState.value.copy(
      isBandalartDeleteAlertDialogOpened = state,
    )
  }

  fun openEmojiBottomSheet(state: Boolean) {
    _uiState.value = _uiState.value.copy(
      isEmojiBottomSheetOpened = state,
    )
  }

  fun openCellBottomSheet(state: Boolean) {
    _uiState.value = _uiState.value.copy(
      isCellBottomSheetOpened = state,
    )
  }

  fun bottomSheetDataChanged(isBottomSheetDataChangedState: Boolean) {
    _uiState.value = _uiState.value.copy(
      isBottomSheetDataChanged = isBottomSheetDataChangedState,
    )
  }

  fun loadingChanged(isLoadingChanged: Boolean) {
    _uiState.value = _uiState.value.copy(
      isLoading = isLoadingChanged,
    )
  }

  fun showSkeletonChanged(isShowSkeletonChanged: Boolean) {
    _uiState.value = _uiState.value.copy(
      isShowSkeleton = isShowSkeletonChanged,
    )
  }

  fun openNetworkErrorAlertDialog(state: Boolean) {
    _uiState.value = _uiState.value.copy(
      isNetworkErrorAlertDialogOpened = state,
    )
  }

  fun openBandalartListBottomSheet(state: Boolean) {
    _uiState.value = _uiState.value.copy(
      isBandalartListBottomSheetOpened = state,
    )
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
    _uiState.value = _uiState.value.copy(
      shareUrl = "",
    )
  }

  fun insertCompletedBandalartKey(bandalartKey: String) {
    viewModelScope.launch {
      insertCompletedBandalartKeyUseCase(bandalartKey)
    }
  }

  suspend fun checkCompletedBandalartKey(bandalartKey: String): Boolean {
    return viewModelScope.async {
      checkCompletedBandalartKeyUseCase(bandalartKey)
    }.await()
  }
}
