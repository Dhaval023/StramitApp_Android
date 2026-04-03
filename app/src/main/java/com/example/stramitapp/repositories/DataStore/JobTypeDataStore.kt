package com.example.stramitapp.repositories

import android.util.Log
import com.example.stramitapp.dao.JobTypeDao
import com.example.stramitapp.models.JobType
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.utilities.AppSettings

class JobTypeDataStore :
    BaseRepository<JobType>(),
    IDataStore<JobType> {

    private val dao: JobTypeDao
        get() = AppSettings.database.jobTypeDao()

    override suspend fun getItemAsync(id: Int): JobType? {
        return dao.getById(id)
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<JobType> {
        return dao.getAll()
    }

    override suspend fun addItemAsync(item: JobType): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("JobTypeDS", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: JobType): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: JobType): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun clearAsync(): Boolean {
        return try {
            dao.clearAll()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun initializeAsync() {}
    override suspend fun pullLatestAsync(): Boolean = false
    override suspend fun syncAsync(): Boolean = false
}