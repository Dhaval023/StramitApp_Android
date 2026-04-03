package com.example.stramitapp.repositories.DataStore

import android.util.Log
import com.example.stramitapp.dao.LicenseeConfigDao
import com.example.stramitapp.models.LicenseeConfig
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.utilities.AppSettings

class LicenseeConfigDataStore :
    BaseRepository<LicenseeConfig>(),
    IDataStore<LicenseeConfig> {

    private val dao: LicenseeConfigDao
        get() = AppSettings.database.licenseeConfigDao()

    override suspend fun getItemAsync(id: Int): LicenseeConfig? {
        return dao.getById(id.toString())
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<LicenseeConfig> {
        return dao.getAll()
    }

    override suspend fun addItemAsync(item: LicenseeConfig): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("LicenseeConfigDS", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: LicenseeConfig): Boolean {
        return try {
            dao.update(item)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: LicenseeConfig): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun clearAsync(): Boolean {
        return try {
            dao.clearAll()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun initializeAsync() {}
    override suspend fun pullLatestAsync(): Boolean = false
    override suspend fun syncAsync(): Boolean = false
}