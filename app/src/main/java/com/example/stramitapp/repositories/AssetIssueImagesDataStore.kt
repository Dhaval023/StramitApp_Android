package com.example.stramitapp.repositories

import com.example.stramitapp.dao.AssetIssueImagesDao
import com.example.stramitapp.model.AssetIssueImages
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.repositories.Base.BaseRepository

class AssetIssueImagesDataStore :
    BaseRepository<AssetIssueImages>(),
    IDataStore<AssetIssueImages> {

    private val dao: AssetIssueImagesDao by lazy { db.assetIssueImagesDao() }

    // ---------------- EXTRA FUNCTION ----------------

    suspend fun getNewIdAsync(): Int {
        return dao.getNextId() ?: 1
    }

    // ---------------- BASIC ----------------

    override suspend fun getItemAsync(id: Int): AssetIssueImages? {
        return dao.getById(id)
    }

    override suspend fun addItemAsync(item: AssetIssueImages): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: AssetIssueImages): Boolean {
        return try {
            dao.update(item) > 0
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: AssetIssueImages): Boolean {
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

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<AssetIssueImages> {
        return dao.getAll()
    }

    // ---------------- EXPORT ----------------

    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetIssueImages> {
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