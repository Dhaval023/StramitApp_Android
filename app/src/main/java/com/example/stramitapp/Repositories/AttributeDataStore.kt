package com.example.stramitapp.Repositories


import com.example.stramitapp.model.Attributes
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//
//class AttributeDataStore : BaseRepository<Attributes>(), IDataStore<Attributes> {
//
//    suspend fun getItemAsync(id: Int): Attributes? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<Attributes>().firstOrNull { item -> item.id == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: Attributes): Boolean {
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
//    suspend fun updateItemAsync(item: Attributes): Boolean {
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
//    suspend fun deleteItemAsync(item: Attributes): Boolean {
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<Attributes> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<Attributes>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsByTableAsync(idTable: Int): List<Attributes> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<Attributes>().filter { item -> item.tableId == idTable }
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
//}