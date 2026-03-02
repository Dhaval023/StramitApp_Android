package com.example.stramitapp.Repositories

import com.example.stramitapp.model.AssetMemoType
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//import com.example.stramitapp.Utilities.AppSettings


//class AssetMemoTypeDataStore : BaseRepository<AssetMemoType>(), IDataStore<AssetMemoType> {
//
//    suspend fun getItemAsync(id: Int): AssetMemoType? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<AssetMemoType>().firstOrNull { item -> item.memoTypeId == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: AssetMemoType): Boolean {
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
//    suspend fun updateItemAsync(item: AssetMemoType): Boolean {
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
//    suspend fun deleteItemAsync(item: AssetMemoType): Boolean {
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<AssetMemoType> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<AssetMemoType>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetMemoType> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.rawQuery("SELECT * FROM tbl_asset_memo_type WHERE last_update_date > '$lastSyncUpData'")
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getNewIdAsync(): Int {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<AssetMemoType>().maxOfOrNull { item -> item.memoTypeId + 1 } ?: 0
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