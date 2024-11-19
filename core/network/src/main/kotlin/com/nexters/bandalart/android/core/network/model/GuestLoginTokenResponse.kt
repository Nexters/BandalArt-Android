package com.nexters.bandalart.android.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GuestLoginTokenResponse(
    @SerialName("key")
    val key: String,
)
