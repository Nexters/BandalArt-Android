package com.nexters.bandalart.android.core.domain.repository

import com.nexters.bandalart.android.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartListEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartCellEntity

/** 반다라트 API */
interface BandalartRepository {

  /**
   * 반다라트 생성
   */

  /**
   * 반다라트 목록 조회
   */
  suspend fun getBandalartList(): BandalartListEntity?

  /**
   * 반다라트 상세 조회
   *
   * @param bandalartKey 메인 셀 고유 키
   */
  suspend fun getBandalartDetail(bandalartKey: String): BandalartDetailEntity?

  /**
   * 반다라트 수정
   *
   * @param bandalartKey 메인 셀 고유 키
   */

  /**
   * 반다라트 삭제
   *
   * @param bandalartKey 메인 셀 고유 키
   */

  /**
   * 메인 셀 조회
   *
   * @param bandalartKey 메인 셀 고유 키
   */
  suspend fun getBandalartMainCell(bandalartKey: String): BandalartCellEntity?

  /**
   * 셀 조회
   *
   * @param bandalartKey 메인 셀 고유 키
   * @param cellKey 셀 고유 키
   */
  suspend fun getBandalartCell(bandalartKey: String, cellKey: String): BandalartCellEntity?

  /**
   * 셀 수정
   *
   * @param bandalartKey 메인 셀 고유 키
   * @param cellKey 셀 고유 키
   */

  /**
   * 셀 삭제
   * @param bandalartKey 메인 셀 고유 키
   * @param cellKey 셀 고유 키
   */

  /**
   * 최근에 수정한 반다라트 표 고유 키 수정
   */
  suspend fun setRecentBandalartKey(recentBandalartKey: String)

  /**
   * 최근에 수정한 반다라트 표 고유 키 조회
   */
  suspend fun getRecentBandalartKey(): String
}
