package com.example.stramitapp.repositories.DataStore

import com.example.stramitapp.dao.AssetInsuranceInfoDao
import com.example.stramitapp.model.AssetInsuranceInfo
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.repositories.Base.BaseRepository

class AssetInsuranceInfoDataStore :
    BaseRepository<AssetInsuranceInfo>(),
    IDataStore<AssetInsuranceInfo> {

    private val dao: AssetInsuranceInfoDao by lazy { db.assetInsuranceInfoDao() }

    // ---------------- BASIC ----------------

    override suspend fun getItemAsync(id: Int): AssetInsuranceInfo? {
        return dao.getById(id)
    }

    override suspend fun addItemAsync(item: AssetInsuranceInfo): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: AssetInsuranceInfo): Boolean {
        return try {
            dao.update(item) > 0
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: AssetInsuranceInfo): Boolean {
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

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<AssetInsuranceInfo> {
        return dao.getAll()
    }

    // ---------------- EXPORT ----------------

    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetInsuranceInfo> {
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