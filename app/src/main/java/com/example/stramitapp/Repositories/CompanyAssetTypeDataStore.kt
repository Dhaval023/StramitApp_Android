package com.example.stramitapp.Repositories


import com.example.stramitapp.model.CompanyAssetType
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//
//class CompanyAssetTypeDataStore : BaseRepository<CompanyAssetType>(), IDataStore<CompanyAssetType> {
//
//    suspend fun getItemAsync(id: Int): CompanyAssetType? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<CompanyAssetType>().firstOrNull { item -> item.id == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: CompanyAssetType): Boolean {
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
//    suspend fun updateItemAsync(item: CompanyAssetType): Boolean {
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
//    suspend fun deleteItemAsync(item: CompanyAssetType): Boolean {
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
//            conn.use { it.deleteAll<CompanyAssetType>() }
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<CompanyAssetType> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<CompanyAssetType>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsAsync(companyId: Int): List<CompanyAssetType> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<CompanyAssetType>().filter { item -> item.companyId == companyId }
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