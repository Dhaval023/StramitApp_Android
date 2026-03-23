package com.example.stramitapp.repositories.DataStore

import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.models.WpCompany
import com.example.stramitapp.utilities.AppSettings.database

class WpCompanyDataStore : BaseRepository<WpCompany>(), IDataStore<WpCompany> {

    private val dao = database.wpCompanyDao()

    override suspend fun getItemAsync(id: Int): WpCompany? {
        return dao.getItem(id)
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<WpCompany> {
        return dao.getAll()
    }

    override suspend fun addItemAsync(item: WpCompany): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: WpCompany): Boolean {
        return try {
            dao.update(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: WpCompany): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun clearAsync(): Boolean {
        return try {
            dao.clear()
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun initializeAsync() {
        throw NotImplementedError()
    }

    override suspend fun pullLatestAsync(): Boolean {
        throw NotImplementedError()
    }

    override suspend fun syncAsync(): Boolean {
        throw NotImplementedError()
    }
}