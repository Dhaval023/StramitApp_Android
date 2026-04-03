package com.example.stramitapp.repositories.DataStore

import android.util.Log
import com.example.stramitapp.dao.MobileJobDao
import com.example.stramitapp.models.MobileJob
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.utilities.AppSettings

class MobileJobDataStore :
    BaseRepository<MobileJob>(),
    IDataStore<MobileJob> {

    private val dao: MobileJobDao
        get() = AppSettings.database.mobileJobDao()

    override suspend fun getItemAsync(id: Int): MobileJob? {
        return dao.getById(id)
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<MobileJob> {
        return dao.getAll()
    }

    override suspend fun addItemAsync(item: MobileJob): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("MobileJobDS", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: MobileJob): Boolean {
        return try {
            dao.update(item)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: MobileJob): Boolean {
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