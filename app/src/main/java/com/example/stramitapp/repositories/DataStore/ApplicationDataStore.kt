package com.example.stramitapp.repositories

import android.util.Log
import com.example.stramitapp.dao.ApplicationDataDao
import com.example.stramitapp.model.Application
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.utilities.AppSettings

class ApplicationDataStore :
    BaseRepository<Application>(),
    IDataStore<Application> {

    private val dao: ApplicationDataDao
        get() = AppSettings.database.applicationDao()

    override suspend fun getItemAsync(id: Int): Application? {
        return dao.getById(id)
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<Application> {
        return dao.getAll()
    }

    suspend fun getNewIdAsync(): Int {
        return (dao.getMaxId() ?: 0) + 1
    }

    override suspend fun addItemAsync(item: Application): Boolean {
        return try {
            item.appId = getNewIdAsync()
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("ApplicationDataStore", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: Application): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("ApplicationDataStore", "Update failed", e)
            false
        }
    }

    override suspend fun deleteItemAsync(item: Application): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (e: Exception) {
            Log.e("ApplicationDataStore", "Delete failed", e)
            false
        }
    }

    override suspend fun clearAsync(): Boolean {
        return try {
            dao.clearAll()
            true
        } catch (e: Exception) {
            Log.e("ApplicationDataStore", "Clear failed", e)
            false
        }
    }


    suspend fun count(): Int = dao.count()

    suspend fun getMaxId(): Int = dao.getMaxId() ?: 0

    override suspend fun initializeAsync() {}

    override suspend fun pullLatestAsync(): Boolean = false

    override suspend fun syncAsync(): Boolean = false
}