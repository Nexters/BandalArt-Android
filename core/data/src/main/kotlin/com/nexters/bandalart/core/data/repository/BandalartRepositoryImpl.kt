package com.nexters.bandalart.core.data.repository

import com.nexters.bandalart.core.data.mapper.toDto
import com.nexters.bandalart.core.data.mapper.toEntity
import com.nexters.bandalart.core.database.BandalartDao
import com.nexters.bandalart.core.datastore.datasource.CompletedBandalartIdDataSource
import com.nexters.bandalart.core.datastore.datasource.RecentBandalartIdDataSource
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.core.domain.entity.BandalartEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartMainCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartSubCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartTaskCellEntity
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import javax.inject.Inject

internal class BandalartRepositoryImpl @Inject constructor(
    private val recentBandalartIdDataSource: RecentBandalartIdDataSource,
    private val completedBandalartIdDataSource: CompletedBandalartIdDataSource,
    private val bandalartDao: BandalartDao,
) : BandalartRepository {
    override suspend fun createBandalart(): BandalartEntity {
        val bandalartId = bandalartDao.createEmptyBandalart()
        return bandalartDao.getBandalart(bandalartId).toEntity()
    }

    override suspend fun getBandalartList(): List<BandalartDetailEntity> {
        return bandalartDao.getBandalartList().map { it.toEntity() }
    }

    override suspend fun getBandalartDetail(bandalartId: Long): BandalartDetailEntity? {
        return bandalartDao.getBandalartDetail(bandalartId)?.toEntity()
    }

    override suspend fun deleteBandalart(bandalartId: Long) {
        bandalartDao.getBandalart(bandalartId).let {
            bandalartDao.deleteBandalart(it)
        }
    }

    override suspend fun getBandalartMainCell(bandalartId: Long): BandalartCellEntity {
        return bandalartDao.getBandalartMainCell(bandalartId).cell.toEntity()
    }

    override suspend fun getChildCells(parentId: Long): List<BandalartCellEntity> {
        val childCells = bandalartDao.getChildCells(parentId)
        return childCells.map { it.toEntity() }
    }

    override suspend fun updateBandalartMainCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartMainCellEntity: UpdateBandalartMainCellEntity,
    ) {
        bandalartDao.updateMainCellWithDto(
            cellId,
            updateBandalartMainCellEntity.toDto(),
        )
    }

    override suspend fun updateBandalartSubCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartSubCellEntity: UpdateBandalartSubCellEntity,
    ) {
        bandalartDao.updateSubCellWithDto(
            cellId,
            updateBandalartSubCellEntity.toDto(),
        )
    }

    override suspend fun updateBandalartTaskCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartTaskCellEntity: UpdateBandalartTaskCellEntity,
    ) {
        bandalartDao.updateTaskCellWithDto(
            cellId,
            updateBandalartTaskCellEntity.toDto(),
        )
    }

    override suspend fun updateBandalartEmoji(
        bandalartId: Long,
        cellId: Long,
        updateBandalartEmojiEntity: UpdateBandalartEmojiEntity,
    ) {
        bandalartDao.updateEmojiWithDto(
            cellId,
            updateBandalartEmojiEntity.toDto(),
        )
    }

    override suspend fun deleteBandalartCell(bandalartId: Long, cellId: Long) {
        bandalartDao.deleteBandalartCell(cellId)
    }

    override suspend fun setRecentBandalartId(recentBandalartId: Long) {
        recentBandalartIdDataSource.setRecentBandalartId(recentBandalartId)
    }

    override suspend fun getRecentBandalartId(): Long {
        return recentBandalartIdDataSource.getRecentBandalartId()
    }

    override suspend fun getPrevBandalartList(): List<Pair<Long, Boolean>> {
        return completedBandalartIdDataSource.getPrevBandalartList()
    }

    override suspend fun upsertBandalartId(bandalartId: Long, isCompleted: Boolean) {
        completedBandalartIdDataSource.upsertBandalartId(bandalartId, isCompleted)
    }

    override suspend fun checkCompletedBandalartId(bandalartId: Long): Boolean {
        return completedBandalartIdDataSource.checkCompletedBandalartId(bandalartId)
    }

    override suspend fun deleteBandalartId(bandalartId: Long) {
        completedBandalartIdDataSource.deleteBandalartId(bandalartId)
    }
}
