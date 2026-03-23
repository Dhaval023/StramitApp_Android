package com.example.stramitapp.repositories

import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.dao.AssetMemoTypeDao
import com.example.stramitapp.model.AssetMemoType

class AssetMemoTypeDataStore(
    private val dao: AssetMemoTypeDao
) : IDataStore<AssetMemoType> {

    override suspend fun getItemAsync(id: Int): AssetMemoType? {
        return try {
            dao.getById(id)
        } catch (ex: Exception) {
            throw ex
        }
    }

    override suspend fun addItemAsync(item: AssetMemoType): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: AssetMemoType): Boolean {
        return try {
            dao.update(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: AssetMemoType): Boolean {
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

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<AssetMemoType> {
        return try {
            dao.getAll()
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetMemoType> {
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