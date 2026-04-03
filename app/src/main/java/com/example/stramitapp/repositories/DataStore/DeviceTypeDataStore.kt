package com.example.stramitapp.repositories

import android.util.Log
import com.example.stramitapp.dao.DeviceTypeDao
import com.example.stramitapp.model.DeviceType
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.utilities.AppSettings

class DeviceTypeDataStore :
    BaseRepository<DeviceType>(),
    IDataStore<DeviceType> {

    private val dao: DeviceTypeDao
        get() = AppSettings.database.deviceTypeDao()

    override suspend fun getItemAsync(id: Int): DeviceType? {
        return dao.getById(id)
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<DeviceType> {
        return dao.getAll()
    }

    override suspend fun addItemAsync(item: DeviceType): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("DeviceTypeDS", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: DeviceType): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: DeviceType): Boolean {
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