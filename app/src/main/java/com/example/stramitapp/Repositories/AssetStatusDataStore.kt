package com.example.stramitapp.Repositories


import com.example.stramitapp.model.AssetStatus
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//
//class AssetStatusDataStore : BaseRepository<AssetStatus>(), IDataStore<AssetStatus> {
//
//    suspend fun getItemAsync(id: Int): AssetStatus? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<AssetStatus>().firstOrNull { item -> item.id == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: AssetStatus): Boolean {
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
//    suspend fun updateItemAsync(item: AssetStatus): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val rowsAffected = it.update(item)
//                rowsAffected > 0
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun deleteItemAsync(item: AssetStatus): Boolean {
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<AssetStatus> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<AssetStatus>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsByCompanyAsync(companyId: Int): List<AssetStatus> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<AssetStatus>().filter { item -> item.companyId == companyId }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getFirstItemByCompanyAsync(companyId: Int): AssetStatus? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<AssetStatus>().firstOrNull { item -> item.companyId == companyId }
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