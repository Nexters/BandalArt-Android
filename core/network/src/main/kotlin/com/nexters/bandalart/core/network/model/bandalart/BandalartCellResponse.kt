package com.nexters.bandalart.core.network.model.bandalart

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BandalartCellResponse(
    @SerialName("key")
    val key: String,
    @SerialName("title")
    val title: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("dueDate")
    val dueDate: String?,
    @SerialName("isCompleted")
    val isCompleted: Boolean,
    @SerialName("completionRatio")
    val completionRatio: Int,
    @SerialName("profileEmoji")
    val profileEmoji: String?,
    @SerialName("mainColor")
    val mainColor: String?,
    @SerialName("subColor")
    val subColor: String?,
    @SerialName("parentKey")
    val parentKey: String?,
    @SerialName("children")
    val children: List<BandalartCellResponse>,
)
