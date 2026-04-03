package com.example.stramitapp.repositories.DataStore

import android.util.Log
import com.example.stramitapp.dao.RoleToRightsMappingDao
import com.example.stramitapp.model.RoleToRightsMapping
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.utilities.AppSettings

class RoleToRightsMappingDataStore :
    BaseRepository<RoleToRightsMapping>(),
    IDataStore<RoleToRightsMapping> {

    private val dao: RoleToRightsMappingDao
        get() = AppSettings.database.roleToRightsMappingDao()

    override suspend fun getItemAsync(id: Int): RoleToRightsMapping? {
        return dao.getById(id)
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<RoleToRightsMapping> {
        return dao.getAll()
    }

    override suspend fun addItemAsync(item: RoleToRightsMapping): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("RoleRightsDS", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: RoleToRightsMapping): Boolean {
        return try {
            dao.insert(item) // REPLACE
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: RoleToRightsMapping): Boolean {
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