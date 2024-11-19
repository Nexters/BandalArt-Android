package com.nexters.bandalart.core.network.model.bandalart

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BandalartDetailResponse(
    @SerialName("key")
    val key: String,
    @SerialName("mainColor")
    val mainColor: String,
    @SerialName("subColor")
    val subColor: String,
    @SerialName("profileEmoji")
    val profileEmoji: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("cellKey")
    val cellKey: String,
    @SerialName("dueDate")
    val dueDate: String?,
    @SerialName("isCompleted")
    val isCompleted: Boolean,
    @SerialName("completionRatio")
    val completionRatio: Int,
)
