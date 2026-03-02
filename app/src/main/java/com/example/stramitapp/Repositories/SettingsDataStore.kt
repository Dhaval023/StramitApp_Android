package com.example.stramitapp.Repositories.DataStore

import com.example.stramitapp.model.Settings
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//
//class SettingsDataStore : BaseRepository<Settings>(), IDataStore<Settings> {
//
//    suspend fun getItemAsync(id: Int): Settings? {
//        throw NotImplementedError("getItemAsync(id) is not implemented")
//    }
//
//    suspend fun getItemAsync(key: String): Settings? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<Settings>().firstOrNull { item -> item.key == key } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: Settings): Boolean {
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
//    suspend fun updateItemAsync(item: Settings): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val rowsAffected = it.insertOrReplace(item)
//                rowsAffected == 1
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun deleteItemAsync(item: Settings): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val rowsAffected = it.delete(item)
//                rowsAffected == 1
//            }
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<Settings> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<Settings>() }
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
//}