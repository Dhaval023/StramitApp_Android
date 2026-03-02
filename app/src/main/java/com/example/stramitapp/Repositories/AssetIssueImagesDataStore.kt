package com.example.stramitapp.Repositories

import com.example.stramitapp.model.AssetIssueImages
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//
//class AssetIssueImagesDataStore : BaseRepository<AssetIssueImages>(), IDataStore<AssetIssueImages> {
//
//    suspend fun getNewIdAsync(): Int {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<AssetIssueImages>().maxOfOrNull { item -> item.id + 1 } ?: 0
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemAsync(id: Int): AssetIssueImages? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<AssetIssueImages>().firstOrNull { item -> item.id == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: AssetIssueImages): Boolean {
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
//    suspend fun updateItemAsync(item: AssetIssueImages): Boolean {
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
//    suspend fun deleteItemAsync(item: AssetIssueImages): Boolean {
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<AssetIssueImages> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<AssetIssueImages>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    // TODO: WHERE last_update_date > LastSyncUpData from config file
//    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetIssueImages> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.rawQuery("SELECT * FROM tbl_asset_movement_info WHERE last_update_date > '$lastSyncUpData'")
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