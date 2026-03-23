package com.example.stramitapp.repositories

import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.dao.AssetMaintenanceInfoDao
import com.example.stramitapp.model.AssetMaintenanceInfo

class AssetMaintenanceInfoDataStore(
    private val dao: AssetMaintenanceInfoDao
) : IDataStore<AssetMaintenanceInfo> {

    override suspend fun getItemAsync(id: Int): AssetMaintenanceInfo? {
        return try {
            dao.getById(id)
        } catch (ex: Exception) {
            throw ex
        }
    }

    override suspend fun addItemAsync(item: AssetMaintenanceInfo): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: AssetMaintenanceInfo): Boolean {
        return try {
            dao.update(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: AssetMaintenanceInfo): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun clearAsync(): Boolean {
        return try {
            dao.clear()
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<AssetMaintenanceInfo> {
        return try {
            dao.getAll()
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetMaintenanceInfo> {
        return try {
            dao.getItemsToExport(lastSyncUpData)
        } catch (ex: Exception) {
            throw ex
        }
    }

    override suspend fun initializeAsync() {}

    override suspend fun pullLatestAsync(): Boolean {
        return false
    }

    override suspend fun syncAsync(): Boolean {
        return false
    }
}