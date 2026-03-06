package com.example.stramitapp.Repositories.DataStore

import com.example.stramitapp.models.JobType
//import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//
//class JobTypeDataStore : BaseRepository<JobType>(), IDataStore<JobType> {
//
//    suspend fun getItemAsync(id: Int): JobType? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<JobType>().firstOrNull { item -> item.jobTypeId == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<JobType> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<JobType>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: JobType): Boolean {
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
//    suspend fun updateItemAsync(item: JobType): Boolean {
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
//    suspend fun deleteItemAsync(item: JobType): Boolean {
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