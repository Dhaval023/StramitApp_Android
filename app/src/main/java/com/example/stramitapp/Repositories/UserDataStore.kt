package com.example.stramitapp.Repositories

import com.example.stramitapp.model.User
import com.example.stramitapp.models.Database.AppDatabase

class UserDataStore {

    private val dao = AppDatabase.getInstance().userDao()

    suspend fun getItemAsync(id: Int): User? {
        return dao.getItem(id)
    }

    suspend fun addItemAsync(item: User): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun updateItemAsync(item: User): Boolean {
        return try {
            dao.update(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun deleteItemAsync(item: User): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun clearAsync(): Boolean {
        return try {
            dao.clear()
            true
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun getItemsAsync(): List<User> {
        return dao.getAll()
    }

    suspend fun getItemsAsync(licenseeId: Int): List<User> {
        return dao.getByLicenseeId(licenseeId)
    }

    suspend fun hasItems(): Boolean {
        return dao.count() > 0
    }

    suspend fun hasItem(user: User): Boolean {
        return dao.countByUserId(user.userId) > 0
    }
}