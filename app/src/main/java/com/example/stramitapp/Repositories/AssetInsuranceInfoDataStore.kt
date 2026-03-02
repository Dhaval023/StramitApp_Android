package com.example.stramitapp.Repositories.DataStore

import com.example.stramitapp.model.AssetInsuranceInfo
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore

//class AssetInsuranceInfoDataStore : BaseRepository<AssetInsuranceInfo>(), IDataStore<AssetInsuranceInfo> {
//
//    suspend fun getItemAsync(id: Int): AssetInsuranceInfo? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<AssetInsuranceInfo>().firstOrNull { item -> item.id == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: AssetInsuranceInfo): Boolean {
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
//    suspend fun updateItemAsync(item: AssetInsuranceInfo): Boolean {
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
//    suspend fun deleteItemAsync(item: AssetInsuranceInfo): Boolean {
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<AssetInsuranceInfo> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<AssetInsuranceInfo>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    // TODO: WHERE last_update_date > LastSyncUpData from config file
//    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetInsuranceInfo> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.rawQuery("SELECT * FROM tbl_asset_insurance_info WHERE last_update_date > '$lastSyncUpData'")
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