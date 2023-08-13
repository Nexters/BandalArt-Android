package com.nexters.bandalart.android.core.domain.repository

import com.nexters.bandalart.android.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartShareEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartMainCellEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartSubCellEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartTaskCellEntity

/** 반다라트 API */
interface BandalartRepository {

  /**
   * 반다라트 생성
   */
  suspend fun createBandalart(): BandalartEntity?

  /**
   * 반다라트 목록 조회
   */
  suspend fun getBandalartList(): List<BandalartDetailEntity>?

  /**
   * 반다라트 상세 조회
   *
   * @param bandalartKey 반다라트 고유 키
   */
  suspend fun getBandalartDetail(bandalartKey: String): BandalartDetailEntity?

  /**
   * 반다라트 삭제
   *
   * @param bandalartKey 반다라트 고유 키
   */
  suspend fun deleteBandalart(bandalartKey: String)

  /**
   * 반다라트 메인 셀 조회
   *
   * @param bandalartKey 반다라트 고유 키
   */
  suspend fun getBandalartMainCell(bandalartKey: String): BandalartCellEntity?

  /**
   * 반다라트 셀 조회
   *
   * @param bandalartKey 반다라트 고유 키
   * @param cellKey 셀 고유 키
   */
  suspend fun getBandalartCell(bandalartKey: String, cellKey: String): BandalartCellEntity?

  /**
   * 반다라트 메인 셀 수정
   *
   * @param bandalartKey 반다라트 고유 키
   * @param cellKey 메인 셀 고유 키
   */
  suspend fun updateBandalartMainCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartMainCellEntity: UpdateBandalartMainCellEntity,
  )

  /**
   * 반다라트 서브 셀 수정
   *
   * @param bandalartKey 반다라트 고유 키
   * @param cellKey 서브 셀 고유 키
   */
  suspend fun updateBandalartSubCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartSubCellEntity: UpdateBandalartSubCellEntity,
  )

  /**
   * 반다라트 태스크 셀 수정
   *
   * @param bandalartKey 빈디라트 고유 키
   * @param cellKey 테스크 셀 고유 키
   */
  suspend fun updateBandalartTaskCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartTaskCellEntity: UpdateBandalartTaskCellEntity,
  )

  /**
   * 반다라트 태스크 셀 수정
   *
   * @param bandalartKey 빈디라트 고유 키
   * @param cellKey 테스크 셀 고유 키
   */
  suspend fun updateBandalartEmoji(
    bandalartKey: String,
    cellKey: String,
    updateBandalartEmojiEntity: UpdateBandalartEmojiEntity,
  )

  /**
   * 반다라트 셀 삭제
   * @param bandalartKey 메인 셀 고유 키
   * @param cellKey 셀 고유 키
   */
  suspend fun deleteBandalartCell(bandalartKey: String, cellKey: String)

  /**
   * 최근에 수정한 반다라트 고유 키 수정
   * @param recentBandalartKey 최근에 수정한 반다라트 고유키
   */
  suspend fun setRecentBandalartKey(recentBandalartKey: String)

  /**
   * 최근에 수정한 반다라트 고유 키 조회
   */
  suspend fun getRecentBandalartKey(): String

  /**
   * 반다라트 공유
   * @param bandalartKey 반다라트 고유 키
   */
  suspend fun shareBandalart(bandalartKey: String): BandalartShareEntity?

  /**
   * 목표를 달성한 반다라트를 목표를 달성한 반다라트 목록에 저장
   * @param bandalartKey 반다라트 고유 키
   */
  suspend fun insertCompletedBandalartKey(bandalartKey: String)

  /**
   * 이미 목표를 달성한 반다라트 인지 여부 확인
   * @param bandalartKey 반다라트 고유 키
   */
  suspend fun checkCompletedBandalartKey(bandalartKey: String): Boolean

  /**
   * 목표 달성 화면을 띄워줬던 반다라트를 목표를 달성한 반다라트 목록에서 제거
   * @param bandalartKey 반다라트 고유 키
   */
  suspend fun deleteCompletedBandalartKey(bandalartKey: String)
}
