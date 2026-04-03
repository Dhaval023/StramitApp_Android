package com.example.stramitapp.repositories

import android.util.Log
import com.example.stramitapp.dao.RightsDao
import com.example.stramitapp.model.Rights
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.utilities.AppSettings

class RightsDataStore :
    BaseRepository<Rights>(),
    IDataStore<Rights> {

    private val dao: RightsDao
        get() = AppSettings.database.rightsDao()

    override suspend fun getItemAsync(id: Int): Rights? {
        return dao.getById(id)
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<Rights> {
        return dao.getAll()
    }

    override suspend fun addItemAsync(item: Rights): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("RightsDS", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: Rights): Boolean {
        return try {
            dao.update(item)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: Rights): Boolean {
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