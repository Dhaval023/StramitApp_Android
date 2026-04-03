package com.example.stramitapp.repositories

import android.util.Log
import com.example.stramitapp.dao.JobDescriptionDao
import com.example.stramitapp.models.JobDescription
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.utilities.AppSettings

class JobDescriptionDataStore :
    BaseRepository<JobDescription>(),
    IDataStore<JobDescription> {

    private val dao: JobDescriptionDao
        get() = AppSettings.database.jobDescriptionDao()

    override suspend fun getItemAsync(id: Int): JobDescription? {
        return dao.getById(id)
    }

    suspend fun getItemAsyncByDesc(desc: String): JobDescription? {
        return dao.getByDesc(desc)
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<JobDescription> {
        return dao.getAll()
    }

    override suspend fun addItemAsync(item: JobDescription): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("JobDescDS", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: JobDescription): Boolean {
        return try {
            dao.insert(item) // replace
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: JobDescription): Boolean {
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