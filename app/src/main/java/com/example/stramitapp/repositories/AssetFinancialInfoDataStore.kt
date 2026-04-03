package com.example.stramitapp.repositories.datastore

import com.example.stramitapp.model.AssetFinancialInfo
import com.example.stramitapp.dao.AssetFinancialInfoDao
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.repositories.Base.BaseRepository

class AssetFinancialInfoDataStore :
    BaseRepository<AssetFinancialInfo>(),
    IDataStore<AssetFinancialInfo> {

    private val dao: AssetFinancialInfoDao by lazy { db.assetFinancialInfoDao() }

    override suspend fun getItemAsync(id: Int): AssetFinancialInfo? {
        return dao.getById(id)
    }

    override suspend fun addItemAsync(item: AssetFinancialInfo): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: AssetFinancialInfo): Boolean {
        return try {
            dao.update(item) > 0
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: AssetFinancialInfo): Boolean {
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

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<AssetFinancialInfo> {
        return dao.getAll()
    }


    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetFinancialInfo> {
        return dao.getItemsToExport(lastSyncUpData)
    }

    override suspend fun initializeAsync() {}

    override suspend fun pullLatestAsync(): Boolean {
        return false
    }

    override suspend fun syncAsync(): Boolean {
        return false
    }
}