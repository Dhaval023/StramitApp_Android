package com.example.stramitapp.repositories

import android.util.Log
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.model.User

class UserDataStore :
    BaseRepository<User>(),
    IDataStore<User> {

    private val dao by lazy { db.userDao() }

    override suspend fun getItemAsync(id: Int): User? {
        return dao.getItem(id)
    }

    override suspend fun addItemAsync(item: User): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("UserDataStore", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: User): Boolean {
        return try {
            dao.update(item)
            true
        } catch (e: Exception) {
            Log.e("UserDataStore", "Update failed", e)
            false
        }
    }

    override suspend fun deleteItemAsync(item: User): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (e: Exception) {
            Log.e("UserDataStore", "Delete failed", e)
            false
        }
    }

    override suspend fun clearAsync(): Boolean {
        return try {
            dao.clear()
            true
        } catch (e: Exception) {
            Log.e("UserDataStore", "Clear failed", e)
            false
        }
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<User> {
        return dao.getAll()
    }

    suspend fun getUsersByLicensee(licenseeId: Int): List<User> {
        return dao.getByLicenseeId(licenseeId)
    }

    suspend fun hasItems(): Boolean {
        return dao.count() > 0
    }

    suspend fun hasItem(user: User): Boolean {
        return dao.countByUserId(user.userId) > 0
    }

    override suspend fun initializeAsync() {}

    override suspend fun pullLatestAsync(): Boolean = false

    override suspend fun syncAsync(): Boolean = false
}