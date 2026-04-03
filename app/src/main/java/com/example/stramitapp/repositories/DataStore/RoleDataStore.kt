package com.example.stramitapp.repositories

import android.util.Log
import com.example.stramitapp.dao.RoleDao
import com.example.stramitapp.model.Role
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.utilities.AppSettings

class RoleDataStore :
    BaseRepository<Role>(),
    IDataStore<Role> {

    private val dao: RoleDao
        get() = AppSettings.database.roleDao()

    override suspend fun getItemAsync(id: Int): Role? {
        return dao.getById(id)
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<Role> {
        return dao.getAll()
    }

    override suspend fun addItemAsync(item: Role): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("RoleDS", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: Role): Boolean {
        return try {
            dao.update(item)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: Role): Boolean {
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