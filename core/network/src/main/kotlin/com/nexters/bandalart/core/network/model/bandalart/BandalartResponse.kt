package com.nexters.bandalart.core.network.model.bandalart

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BandalartResponse(
    @SerialName("key")
    val key: String,
    @SerialName("mainColor")
    val mainColor: String,
    @SerialName("subColor")
    val subColor: String,
    @SerialName("profileEmoji")
    val profileEmoji: String?,
    @SerialName("completionRatio")
    val completionRatio: Int,
)
