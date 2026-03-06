package com.example.stramitapp.Repositories

import com.example.stramitapp.model.Company
import com.example.stramitapp.model.CompanyAssetCategory
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//
//class CompanyAssetCategoryDataStore : BaseRepository<CompanyAssetCategory>(), IDataStore<CompanyAssetCategory> {
//
//    suspend fun getItemsAsync(companyId: Int): List<CompanyAssetCategory> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<CompanyAssetCategory>().filter { item -> item.companyId == companyId }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemAsync(id: Int): CompanyAssetCategory? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<CompanyAssetCategory>().firstOrNull { item -> item.id == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: CompanyAssetCategory): Boolean {
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
//    suspend fun updateItemAsync(item: CompanyAssetCategory): Boolean {
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
//    suspend fun deleteItemAsync(item: CompanyAssetCategory): Boolean {
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
//            conn.use { it.deleteAll<Company>() }
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<CompanyAssetCategory> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<CompanyAssetCategory>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getFirstItemByCompanyAsync(companyId: Int): CompanyAssetCategory? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<CompanyAssetCategory>().firstOrNull { item -> item.companyId == companyId }
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