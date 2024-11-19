package com.nexters.bandalart.core.data.mapper

import com.nexters.bandalart.core.network.model.GuestLoginTokenResponse
import com.nexters.bandalart.core.domain.entity.GuestLoginTokenEntity

internal fun GuestLoginTokenResponse.toEntity() =
    GuestLoginTokenEntity(key = key)
