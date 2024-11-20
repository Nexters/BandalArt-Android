package com.nexters.bandalart.feature.home.model

data class BandalartCellUiModel(
    val id: Long = 0L,
    val title: String? = null,
    val description: String? = null,
    val dueDate: String? = null,
    val isCompleted: Boolean = false,
    val parentId: Long? = 0L,
    val children: List<BandalartCellUiModel> = emptyList()
) {
    fun copy(): BandalartCellUiModel = BandalartCellUiModel(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        isCompleted = isCompleted,
        parentId = parentId,
        children = children
    )
}
