package com.nexters.bandalart.android.core.data.mapper

import com.nexters.bandalart.android.core.data.model.GuestLoginTokenResponse
import com.nexters.bandalart.android.core.domain.entity.GuestLoginTokenEntity

internal fun GuestLoginTokenResponse.toEntity() =
  GuestLoginTokenEntity(key = key)
