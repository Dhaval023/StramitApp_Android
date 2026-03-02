package com.example.stramitapp.Repositories

import com.example.stramitapp.model.User
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//
//class UserDataStore : BaseRepository<User>(), IDataStore<User> {
//
//    suspend fun getItemAsync(id: Int): User? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<User>().firstOrNull { item -> item.userId == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: User): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use { it.insert(item) }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun updateItemAsync(item: User): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use { it.update(item) }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun deleteItemAsync(item: User): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use { it.delete(item) }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun clearAsync(): Boolean {
//        throw NotImplementedError("clearAsync is not implemented")
//    }
//
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<User> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<User>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsAsync(licenseeId: Int): List<User> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<User>().filter { item -> item.licenseeId == licenseeId }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun initializeAsync() {
//        throw NotImplementedError("initializeAsync is not implemented")
//    }
//
//    suspend fun pullLatestAsync(): Boolean {
//        throw NotImplementedError("pullLatestAsync is not implemented")
//    }
//
//    suspend fun syncAsync(): Boolean {
//        throw NotImplementedError("syncAsync is not implemented")
//    }
//
//    fun hasItems(): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<User>().isNotEmpty() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    fun hasItem(user: User): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<User>().any { item -> item.userId == user.userId }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//}