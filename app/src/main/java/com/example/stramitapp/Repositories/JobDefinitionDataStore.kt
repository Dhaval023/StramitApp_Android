package com.example.stramitapp.Repositories


import JobDefinitionDao
import com.example.stramitapp.model.JobDefinition
import com.example.stramitapp.model.MixJobDefinitionAssigment
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore

abstract class JobDefinitionDataStore(
    private val dao: JobDefinitionDao
) : BaseRepository<JobDefinition>(), IDataStore<JobDefinition> {

   override suspend fun getItemAsync(jobId: Int): JobDefinition? {
        return dao.getItem(jobId)
    }

    suspend fun getItemsAsync(
        jobId: Int,
        companyId: Int?,
        locationId: Int?
    ): List<JobDefinition> {
        return dao.getItems(jobId, companyId, locationId)
    }

    suspend fun getItemsAcceptedAsyncByAssignTo(
        assignTo: Int
    ): List<MixJobDefinitionAssigment> {
        return dao.getAcceptedJobs(assignTo)
    }

   override suspend fun addItemAsync(item: JobDefinition): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

   override suspend fun updateItemAsync(item: JobDefinition): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

   override suspend fun deleteItemAsync(item: JobDefinition): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<JobDefinition> {
        return dao.getAll()
    }

    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<JobDefinition> {
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