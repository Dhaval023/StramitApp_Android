package com.example.stramitapp.Dao

import androidx.room.*
import com.example.stramitapp.model.JobAssetTrackInfo

@Dao
interface JobAssetTrackInfoDao {

    @Query("SELECT * FROM tbl_job_asset_track_info WHERE job_id = :jobId AND asset_id = :assetId LIMIT 1")
    suspend fun getByKey(jobId: Int, assetId: Int): JobAssetTrackInfo?

    @Query("SELECT * FROM tbl_job_asset_track_info WHERE job_id = :jobId")
    suspend fun getByJob(jobId: Int): List<JobAssetTrackInfo>

    @Query("SELECT * FROM tbl_job_asset_track_info WHERE job_id = :jobId AND location_id = :locationId")
    suspend fun getByJobLocation(jobId: Int, locationId: Int): List<JobAssetTrackInfo>

    @Query("SELECT * FROM tbl_job_asset_track_info")
    suspend fun getAll(): List<JobAssetTrackInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: JobAssetTrackInfo)

    @Delete
    suspend fun delete(item: JobAssetTrackInfo)

    @Query("SELECT * FROM tbl_job_asset_track_info WHERE last_update_date > :lastSyncUpData")
    suspend fun getItemsToExport(lastSyncUpData: String): List<JobAssetTrackInfo>

    @Query("SELECT * FROM tbl_job_asset_track_info WHERE job_id = :jobId AND last_update_date > :lastSyncUpData")
    suspend fun getItemsToExport(lastSyncUpData: String, jobId: Int): List<JobAssetTrackInfo>
}