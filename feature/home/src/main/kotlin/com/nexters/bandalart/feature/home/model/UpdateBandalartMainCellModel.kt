package com.nexters.bandalart.feature.home.model

data class UpdateBandalartMainCellModel(
    val title: String? = "",
    val description: String? = null,
    val dueDate: String? = null,
    val profileEmoji: String? = "",
    val mainColor: String,
    val subColor: String,
)
