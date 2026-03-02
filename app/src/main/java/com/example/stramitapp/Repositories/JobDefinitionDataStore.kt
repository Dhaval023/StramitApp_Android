package com.example.stramitapp.Repositories


import com.example.stramitapp.model.JobDefinition
import com.example.stramitapp.model.MixJobDefinitionAssigment
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//
//class JobDefinitionDataStore : BaseRepository<JobDefinition>(), IDataStore<JobDefinition> {
//
//    suspend fun getItemAsync(jobId: Int): JobDefinition? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<JobDefinition>().firstOrNull { item -> item.jobId == jobId } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsAsync(jobId: Int, companyId: Int?, locationId: Int?): List<JobDefinition> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                var result = it.queryAll<JobDefinition>().filter { item -> item.jobId == jobId }
//                if (companyId != null) result = result.filter { item -> item.companyId == companyId }
//                if (locationId != null) result = result.filter { item -> item.locationId == locationId }
//                result
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsAcceptedAsyncByAssignTo(assignTo: Int): List<MixJobDefinitionAssigment> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val sql = """
//                    SELECT jd.job_id as jobId, jd.job_name as jobName, jd.company_id as companyId,
//                           jd.location_id as locationId, jd.job_desc as jobDesc, jd.creation_date as creationDate,
//                           jd.created_by as createdBy, jd.submittion_date as submittionDate, jd.update_flag as updateFlag,
//                           jd.last_update_date as lastUpdateDate, ja.status as assigStatus
//                    FROM tbl_job_definition as jd
//                    INNER JOIN tbl_job_assignment as ja ON ja.job_id = jd.job_id
//                    WHERE ja.assign_to = $assignTo AND ja.status IN (2,4,5)
//                """.trimIndent()
//                it.rawQuery(sql)
//            }
//        } catch (ex: Exception) {
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: JobDefinition): Boolean {
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
//    suspend fun updateItemAsync(item: JobDefinition): Boolean {
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
//    suspend fun deleteItemAsync(item: JobDefinition): Boolean {
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<JobDefinition> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<JobDefinition>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    // TODO: WHERE last_update_date > LastSyncUpData from config file
//    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<JobDefinition> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.rawQuery("SELECT * FROM tbl_job_definition WHERE last_update_date > '$lastSyncUpData'")
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