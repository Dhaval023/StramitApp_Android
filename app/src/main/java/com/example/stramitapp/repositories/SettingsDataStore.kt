package com.example.stramitapp.repositories.DataStore

import com.example.stramitapp.dao.SettingsDao
import com.example.stramitapp.model.Settings
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore

abstract class SettingsDataStore(
    private val dao: SettingsDao
) : BaseRepository<Settings>(), IDataStore<Settings> {

    suspend fun getItemAsync(key: String): Settings? {
        return dao.getItem(key)
    }

   override suspend fun addItemAsync(item: Settings): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

   override suspend fun updateItemAsync(item: Settings): Boolean {
        return try {
            dao.insert(item) // same as InsertOrReplace in Xamarin
            true
        } catch (ex: Exception) {
            false
        }
    }

   override suspend fun deleteItemAsync(item: Settings): Boolean {
        return try {
            dao.delete(item) == 1
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<Settings> {
        return dao.getAll()
    }

    override suspend fun initializeAsync() {
        // Not required in Room
    }

    override suspend fun pullLatestAsync(): Boolean {
        throw NotImplementedError()
    }

    override suspend fun syncAsync(): Boolean {
        throw NotImplementedError()
    }
}