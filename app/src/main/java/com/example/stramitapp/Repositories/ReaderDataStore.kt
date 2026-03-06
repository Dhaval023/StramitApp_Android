package com.example.stramitapp.Repositories

import com.example.stramitapp.model.ReaderDevice
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//
//class ReaderDataStore : BaseRepository<ReaderDevice>(), IDataStore<ReaderDevice> {
//
//    suspend fun getItemAsync(id: Int): ReaderDevice? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<ReaderDevice>().firstOrNull { item -> item.id == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: ReaderDevice): Boolean {
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
//    suspend fun updateItemAsync(item: ReaderDevice): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val rowsAffected = it.update(item)
//                rowsAffected == 1
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun deleteItemAsync(item: ReaderDevice): Boolean {
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<ReaderDevice> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                initializeAsync()
//                it.queryAll<ReaderDevice>()
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsByTableAsync(idTable: Int): List<ReaderDevice> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<ReaderDevice>().filter { item -> item.id == idTable }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    fun initializeAsync() {
//        val conn = getConnection()
//        conn.use {
//            it.createTable<ReaderDevice>()
//        }
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