package com.example.stramitapp.Repositories

import com.example.stramitapp.model.CompanyAssetParts
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//
//class CompanyAssetPartsDataStore : BaseRepository<CompanyAssetParts>(), IDataStore<CompanyAssetParts> {
//
//    suspend fun getItemAsync(partId: Int): CompanyAssetParts? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<CompanyAssetParts>().firstOrNull { item -> item.partId == partId } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: CompanyAssetParts): Boolean {
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
//    suspend fun updateItemAsync(item: CompanyAssetParts): Boolean {
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
//    suspend fun deleteItemAsync(item: CompanyAssetParts): Boolean {
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
//    suspend fun clearAllAsync(): Int {
//        return try {
//            val conn = getConnection()
//            conn.use { it.deleteAll<CompanyAssetParts>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            -1
//        }
//    }
//
//    suspend fun clearAsync(): Boolean {
//        throw NotImplementedError("clearAsync is not implemented")
//    }
//
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<CompanyAssetParts> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<CompanyAssetParts>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    // Function to fetch Asset Parts based on selected device
//    suspend fun getItemsAsyncByDevice(deviceId: Int): List<CompanyAssetParts> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<CompanyAssetParts>()
//                    .filter { item -> item.parentPartId == deviceId && item.updateFlag != "D" }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsAsync(companyId: Int): List<CompanyAssetParts> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<CompanyAssetParts>().filter { item -> item.companyId == companyId }
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