package com.example.stramitapp.repositories.DataStore


import android.util.Log
import com.example.stramitapp.dao.LicenseePeerDao
import com.example.stramitapp.model.LicenseePeer
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.utilities.AppSettings

class LicenseePeerDataStore :
    BaseRepository<LicenseePeer>(),
    IDataStore<LicenseePeer> {

    private val dao: LicenseePeerDao
        get() = AppSettings.database.licenseePeerDao()

    override suspend fun getItemAsync(id: Int): LicenseePeer? {
        return dao.getById(id)
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<LicenseePeer> {
        return dao.getAll()
    }

    override suspend fun addItemAsync(item: LicenseePeer): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("LicenseePeerDS", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: LicenseePeer): Boolean {
        return try {
            dao.update(item)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: LicenseePeer): Boolean {
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