package com.example.stramitapp.Repositories.DataStore

import com.example.stramitapp.Repositories.Base.IDataStore
import com.example.stramitapp.model.Asset
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.model.AssetCondition
//
//class AssetConditionDataStore : BaseRepository<AssetCondition>(), IDataStore<AssetCondition> {
//
//    suspend fun getItemAsync(id: Int): AssetCondition? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.query("SELECT * FROM AssetCondition WHERE Id = ?", arrayOf(id.toString()))
//                    .let { cursor -> cursor.toObject<AssetCondition>() }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: AssetCondition): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.insert(item)
//            }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun updateItemAsync(item: AssetCondition): Boolean {
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
//    suspend fun deleteItemAsync(item: AssetCondition): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.delete(item)
//            }
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<AssetCondition> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<AssetCondition>()
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsByCompanyAsync(companyId: Int): List<AssetCondition> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<AssetCondition>()
//                    .filter { item -> item.companyId == companyId && item.updateFlag != "D" }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getFirstItemByCompanyAsync(companyId: Int): AssetCondition? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<AssetCondition>()
//                    .firstOrNull { item -> item.companyId == companyId }
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
//    // START Asset Status Checking
//    suspend fun getItemsByAssetAsyncStatus(assetUpdateFlag: String): List<Asset> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<Asset>()
//                    .filter { item -> item.updateFlag == assetUpdateFlag && item.updateFlag != "D" }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//    // END Asset Status Checking
//}