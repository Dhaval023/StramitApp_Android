package com.example.stramitapp.Repositories


import com.example.stramitapp.model.AssetMovementInfo
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore

//
//class AssetMovementInfoDataStore : BaseRepository<AssetMovementInfo>(), IDataStore<AssetMovementInfo> {
//
//    suspend fun getItemAsync(id: Int): AssetMovementInfo? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<AssetMovementInfo>().firstOrNull { item -> item.id == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: AssetMovementInfo): Boolean {
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
//    suspend fun updateItemAsync(item: AssetMovementInfo): Boolean {
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
//    suspend fun deleteItemAsync(item: AssetMovementInfo): Boolean {
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<AssetMovementInfo> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<AssetMovementInfo>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    // TODO: WHERE last_update_date > LastSyncUpData from config file
//    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetMovementInfo> {
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
//    suspend fun deleteAssetMovementHistory(): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.execute("DELETE FROM tbl_asset_movement_info")
//            }
//            true
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
//    // TODO: MOVEMENT
//    // suspend fun addMovement(items: List<Any>): Boolean { ... }
//}