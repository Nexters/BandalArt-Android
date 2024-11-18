package com.nexters.bandalart.android.core.data.mapper

//internal fun BandalartResponse.toEntity() =
//  BandalartEntity(
//    key = key,
//    mainColor = mainColor,
//    subColor = subColor,
//    profileEmoji = profileEmoji,
//    completionRatio = completionRatio,
//  )
//
//internal fun BandalartDetailResponse.toEntity() =
//  BandalartDetailEntity(
//    key = key,
//    mainColor = mainColor,
//    subColor = subColor,
//    profileEmoji = profileEmoji,
//    title = title,
//    cellKey = cellKey,
//    dueDate = dueDate,
//    isCompleted = isCompleted,
//    completionRatio = completionRatio,
//  )
//
//// 재귀 호출시 컴파일러가 타입을 추론할 수 없기 때문에 예외적으로 반환타입을 지정
//internal fun BandalartCellResponse.toEntity(): BandalartCellEntity {
//  return BandalartCellEntity(
//    key = key,
//    title = title,
//    description = description,
//    dueDate = dueDate,
//    isCompleted = isCompleted,
//    completionRatio = completionRatio,
//    profileEmoji = profileEmoji,
//    mainColor = mainColor,
//    subColor = subColor,
//    parentKey = parentKey,
//    children = children.map { it.toEntity() },
//  )
//}
//
//internal fun UpdateBandalartMainCellEntity.toModel() =
//  UpdateBandalartMainCellRequest(
//    title = title,
//    description = description,
//    dueDate = dueDate,
//    profileEmoji = profileEmoji,
//    mainColor = mainColor,
//    subColor = subColor,
//  )
//
//internal fun UpdateBandalartSubCellEntity.toModel() =
//  UpdateBandalartSubCellRequest(
//    title = title,
//    description = description,
//    dueDate = dueDate,
//  )
//
//internal fun UpdateBandalartTaskCellEntity.toModel() =
//  UpdateBandalartTaskCellRequest(
//    title = title,
//    description = description,
//    dueDate = dueDate,
//    isCompleted = isCompleted,
//  )
//
//internal fun UpdateBandalartEmojiEntity.toModel() =
//  UpdateBandalartEmojiRequest(profileEmoji = profileEmoji)
//
//internal fun BandalartShareResponse.toEntity() =
//  BandalartShareEntity(
//    shareUrl = shareUrl,
//    key = key,
//    endDate = endDate,
//  )
//
//fun BandalartDBEntity.toEntity(): BandalartEntity = BandalartEntity(
//  id = id,
//  mainColor = mainColor,
//  subColor = subColor,
//  profileEmoji = profileEmoji,
//  completionRatio = completionRatio
//)
//
//fun BandalartEntity.toDBEntity(): BandalartDBEntity = BandalartDBEntity(
//    id = id,
//    mainColor = mainColor,
//    subColor = subColor,
//    profileEmoji = profileEmoji,
//    completionRatio = completionRatio
//  )
//
//fun BandalartDetailDBEntity.toEntity(): BandalartDetailEntity = BandalartDetailEntity(
//  id = id,
//  mainColor = mainColor,
//  subColor = subColor,
//  profileEmoji = profileEmoji,
//  title = title,
//  cellKey = cellId,
//  dueDate = dueDate,
//  isCompleted = isCompleted,
//  completionRatio = completionRatio
//)
//
//fun BandalartDetailEntity.toDBEntity(): BandalartDetailDBEntity = BandalartDetailDBEntity(
//  id = id,
//  mainColor = mainColor,
//  subColor = subColor,
//  profileEmoji = profileEmoji,
//  title = title,
//  cellId = cellKey,
//  dueDate = dueDate,
//  isCompleted = isCompleted,
//  completionRatio = completionRatio
//)


