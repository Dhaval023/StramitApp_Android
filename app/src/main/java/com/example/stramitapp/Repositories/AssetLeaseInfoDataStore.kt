package com.example.stramitapp.Repositories

import com.example.stramitapp.Dao.AssetLeaseInfoDao
import com.example.stramitapp.model.AssetLeaseInfo
import com.example.stramitapp.Repositories.Base.IDataStore

class AssetLeaseInfoDataStore(
    private val dao: AssetLeaseInfoDao
) : IDataStore<AssetLeaseInfo> {

    override suspend fun getItemAsync(id: Int): AssetLeaseInfo? {
        return try {
            dao.getById(id)
        } catch (ex: Exception) {
            throw ex
        }
    }

    override suspend fun addItemAsync(item: AssetLeaseInfo): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: AssetLeaseInfo): Boolean {
        return try {
            dao.update(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: AssetLeaseInfo): Boolean {
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

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<AssetLeaseInfo> {
        return try {
            dao.getAll()
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetLeaseInfo> {
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