package com.example.stramitapp.repositories.DataStore

import android.util.Log
import com.example.stramitapp.dao.UserRoleDao
import com.example.stramitapp.model.UserRole
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.utilities.AppSettings

class UserRoleDataStore :
    BaseRepository<UserRole>(),
    IDataStore<UserRole> {

    private val dao: UserRoleDao
        get() = AppSettings.database.userRoleDao()

    override suspend fun getItemAsync(id: Int): UserRole? {
        return dao.getById(id)
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<UserRole> {
        return dao.getAll()
    }

    override suspend fun addItemAsync(item: UserRole): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("UserRoleDS", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: UserRole): Boolean {
        return try {
            dao.insert(item) // REPLACE
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: UserRole): Boolean {
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