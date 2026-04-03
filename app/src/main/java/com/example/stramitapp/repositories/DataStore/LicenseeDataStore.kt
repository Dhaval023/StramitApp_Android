package com.example.stramitapp.repositories

import android.util.Log
import com.example.stramitapp.dao.LicenseeDao
import com.example.stramitapp.model.Licensee
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.utilities.AppSettings

class LicenseeDataStore :
    BaseRepository<Licensee>(),
    IDataStore<Licensee> {

    private val dao: LicenseeDao
        get() = AppSettings.database.licenseeDao()

    override suspend fun getItemAsync(id: Int): Licensee? {
        return dao.getById(id)
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<Licensee> {
        return dao.getAll()
    }

    override suspend fun addItemAsync(item: Licensee): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("LicenseeDS", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: Licensee): Boolean {
        return try {
            dao.update(item)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: Licensee): Boolean {
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