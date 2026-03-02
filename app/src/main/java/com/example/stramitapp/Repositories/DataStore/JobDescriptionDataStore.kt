package com.example.stramitapp.Repositories.DataStore

import com.example.stramitapp.models.JobDescription
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//
//class JobDescriptionDataStore : BaseRepository<JobDescription>(), IDataStore<JobDescription> {
//
//    suspend fun getItemAsync(id: Int): JobDescription? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<JobDescription>().firstOrNull { item -> item.jobDescId == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemAsyncByDesc(desc: String): JobDescription? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<JobDescription>().firstOrNull { item -> item.jobDesc == desc } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<JobDescription> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<JobDescription>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: JobDescription): Boolean {
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
//    suspend fun updateItemAsync(item: JobDescription): Boolean {
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
//    suspend fun deleteItemAsync(item: JobDescription): Boolean {
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