package com.example.stramitapp.Repositories

import com.example.stramitapp.Dao.AssetMovementInfoDao
import com.example.stramitapp.model.AssetMovementInfo
import com.example.stramitapp.utilities.AppSettings

class AssetMovementInfoDataStore {

     val dao: AssetMovementInfoDao
        get() = AppSettings.database.assetMovementInfoDao()


    suspend fun getItemAsync(id: Int): AssetMovementInfo? {
        return try {
            dao.getById(id)
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun addItemAsync(item: AssetMovementInfo): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            android.util.Log.e("AssetMovementInfoDS", "insert FAILED: ${ex.javaClass.simpleName} — ${ex.message}", ex)
            false
        }
    }

    suspend fun updateItemAsync(item: AssetMovementInfo): Boolean {
        return try {
            dao.update(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun deleteItemAsync(item: AssetMovementInfo): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun getItemsAsync(): List<AssetMovementInfo> {
        return try {
            dao.getAll()
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetMovementInfo> {
        return try {
            dao.getItemsToExport(lastSyncUpData)
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun deleteAssetMovementHistory(): Boolean {
        return try {
            dao.deleteAll()
            true
        } catch (ex: Exception) {
            false
        }
    }
}