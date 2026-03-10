package com.example.stramitapp.Repositories


import com.example.stramitapp.Dao.JobAssetTrackInfoDao
import com.example.stramitapp.model.JobAssetTrackInfo
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore

abstract class JobAssetTrackInfoDataStore(
    private val dao: JobAssetTrackInfoDao
) : BaseRepository<JobAssetTrackInfo>(), IDataStore<JobAssetTrackInfo> {

    suspend fun getItemByKeyAsync(jobId: Int, assetId: Int): JobAssetTrackInfo? {
        return dao.getByKey(jobId, assetId)
    }

    suspend fun getItemsAsync(jobId: Int, assetId: Int, locationId: Int): JobAssetTrackInfo? {
        return dao.getByKey(jobId, assetId)?.takeIf { it.locationId == locationId }
    }

    suspend fun getItemsAsync(jobId: Int, locationId: Int): List<JobAssetTrackInfo> {
        return dao.getByJobLocation(jobId, locationId)
    }

    suspend fun getItemsAsync(jobId: Int): List<JobAssetTrackInfo> {
        return dao.getByJob(jobId)
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<JobAssetTrackInfo> {
        return dao.getAll()
    }

    override suspend fun addItemAsync(item: JobAssetTrackInfo): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: JobAssetTrackInfo): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<JobAssetTrackInfo> {
        return dao.getItemsToExport(lastSyncUpData)
    }

    suspend fun getItemsToExportAsync(lastSyncUpData: String, jobId: Int): List<JobAssetTrackInfo> {
        return dao.getItemsToExport(lastSyncUpData, jobId)
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