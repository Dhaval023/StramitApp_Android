package com.example.stramitapp.Repositories.DataStore

import com.example.stramitapp.Dao.AssetInspectionInfoDao
import com.example.stramitapp.model.AssetInspectionInfo
import com.example.stramitapp.Repositories.Base.IDataStore
import com.example.stramitapp.Repositories.Base.BaseRepository

class AssetInspectionInfoDataStore :
    BaseRepository<AssetInspectionInfo>(),
    IDataStore<AssetInspectionInfo> {

    private val dao: AssetInspectionInfoDao by lazy { db.assetInspectionInfoDao() }

    // ---------------- BASIC ----------------

    override suspend fun getItemAsync(id: Int): AssetInspectionInfo? {
        return dao.getById(id)
    }

    override suspend fun addItemAsync(item: AssetInspectionInfo): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: AssetInspectionInfo): Boolean {
        return try {
            dao.update(item) > 0
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: AssetInspectionInfo): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun clearAsync(): Boolean {
        return try {
            dao.clearAll()
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<AssetInspectionInfo> {
        return dao.getAll()
    }

    // ---------------- EXPORT ----------------

    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetInspectionInfo> {
        return dao.getItemsToExport(lastSyncUpData)
    }

    // ---------------- PLACEHOLDERS ----------------

    override suspend fun initializeAsync() {}

    override suspend fun pullLatestAsync(): Boolean {
        return false
    }

    override suspend fun syncAsync(): Boolean {
        return false
    }
}