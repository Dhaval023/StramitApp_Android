package com.example.stramitapp.Repositories


import com.example.stramitapp.model.JobAssignment
import com.example.stramitapp.Repositories.Base.BaseRepository
//import com.example.stramitapp.Services.API.APIHelper
import com.example.stramitapp.Repositories.Base.IDataStore

//
//class JobAssignmentDataStore : BaseRepository<JobAssignment>(), IDataStore<JobAssignment> {
//
//    suspend fun getItemAsync(id: Int): JobAssignment? {
//        throw NotImplementedError("getItemAsync(id) is not implemented")
//    }
//
//    suspend fun getItemAsync(jobId: Int, assignTo: Int): JobAssignment? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<JobAssignment>()
//                    .firstOrNull { item -> item.jobId == jobId && item.assignTo == assignTo }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemByUserAsync(assignTo: Int): List<JobAssignment> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<JobAssignment>().filter { item -> item.assignTo == assignTo }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsCompleteAsync(assignTo: Int): List<JobAssignment> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<JobAssignment>()
//                    .filter { item ->
//                        item.assignTo == assignTo &&
//                                item.status == APIHelper.StatusJob.Completed.value
//                    }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: JobAssignment): Boolean {
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
//    suspend fun updateItemAsync(item: JobAssignment): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use { it.insertOrReplace(item) }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun deleteItemAsync(item: JobAssignment): Boolean {
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<JobAssignment> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<JobAssignment>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    // TODO: WHERE last_update_date > LastSyncUpData from config file
//    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<JobAssignment> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.rawQuery("SELECT * FROM tbl_job_assignment WHERE last_update_date > '$lastSyncUpData'")
//            }
//        } catch (ex: Exception) {
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