package com.example.stramitapp.Repositories


import com.example.stramitapp.Dao.JobAssignmentDao
import com.example.stramitapp.model.JobAssignment
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
import com.example.stramitapp.services.APIHelper

abstract class JobAssignmentDataStore(
    private val dao: JobAssignmentDao
) : BaseRepository<JobAssignment>(), IDataStore<JobAssignment> {

    suspend fun getItemAsync(jobId: Int, assignTo: Int): JobAssignment? {
        return dao.getItem(jobId, assignTo)
    }

    suspend fun getItemByUserAsync(assignTo: Int): List<JobAssignment> {
        return dao.getByUser(assignTo)
    }

//    suspend fun getItemsCompleteAsync(assignTo: Int): List<JobAssignment> {
//        return dao.getCompleted(assignTo, APIHelper.StatusJob.Completed.value)
//    }

   override suspend fun addItemAsync(item: JobAssignment): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

   override suspend fun updateItemAsync(item: JobAssignment): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: JobAssignment): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<JobAssignment> {
        return dao.getAll()
    }

    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<JobAssignment> {
        return dao.getItemsToExport(lastSyncUpData)
    }

    override suspend fun initializeAsync() {
        throw NotImplementedError()
    }

    override suspend fun pullLatestAsync(): Boolean {
        throw NotImplementedError()
    }

    override suspend fun syncAsync(): Boolean {
        throw NotImplementedError()
    }
}