package com.nexters.bandalart.android.core.domain.repository

import com.nexters.bandalart.android.core.domain.entity.GuestLoginTokenEntity

interface GuestLoginTokenRepository {
  /**
   * 게스트 로그인 토큰 저장
   *
   * @param guestLoginToken 게스트 로그인 토큰
   */
  suspend fun setGuestLoginToken(guestLoginToken: String)

  /**
   * 게스트 로그인 토큰 조회
   */
  suspend fun getGuestLoginToken(): String

  /**
   * 게스트 로그인 토큰 생성
   */
  suspend fun createGuestLoginToken(): GuestLoginTokenEntity?
}
