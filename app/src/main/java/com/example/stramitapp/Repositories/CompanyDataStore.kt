package com.example.stramitapp.Repositories

import com.example.stramitapp.model.Company
import com.example.stramitapp.Repositories.Base.BaseRepository
import  com.example.stramitapp.Repositories.Base.IDataStore
//
//class CompanyDataStore : BaseRepository<Company>(), IDataStore<Company> {
//
//    suspend fun getItemAsync(id: Int): Company? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<Company>().firstOrNull { item -> item.companyId == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    fun getFirstCompany(): Company? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<Company>().firstOrNull() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: Company): Boolean {
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
//    suspend fun updateItemAsync(item: Company): Boolean {
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
//    suspend fun deleteItemAsync(item: Company): Boolean {
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
//    suspend fun resetCompanyAsync(idCompanyKeep: List<Int>): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val itemsToDelete = it.queryAll<Company>()
//                    .filter { item -> !idCompanyKeep.contains(item.companyId) }
//                for (item in itemsToDelete) {
//                    it.execute("DELETE FROM tbl_company WHERE company_id = ${item.companyId}")
//                    it.delete(item)
//                }
//            }
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<Company> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<Company>() }
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