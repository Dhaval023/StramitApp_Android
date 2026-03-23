package com.example.stramitapp.repositories

import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.dao.AssetMemoInfoDao
import com.example.stramitapp.model.AssetMemoInfo

class AssetMemoInfoDataStore(
    private val dao: AssetMemoInfoDao
) : IDataStore<AssetMemoInfo> {

    override suspend fun getItemAsync(id: Int): AssetMemoInfo? {
        return try {
            dao.getById(id)
        } catch (ex: Exception) {
            throw ex
        }
    }

    override suspend fun addItemAsync(item: AssetMemoInfo): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: AssetMemoInfo): Boolean {
        return try {
            dao.update(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: AssetMemoInfo): Boolean {
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

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<AssetMemoInfo> {
        return try {
            dao.getAll()
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetMemoInfo> {
        return try {
            dao.getItemsToExport(lastSyncUpData)
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun getNewIdAsync(): Int {
        return try {
            dao.getNewId() ?: 1
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