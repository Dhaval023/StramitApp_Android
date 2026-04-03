package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.models.JobType

@Dao
interface JobTypeDao {

    @Query("SELECT * FROM tbl_job_type WHERE job_type_id = :id LIMIT 1")
    suspend fun getById(id: Int): JobType?

    @Query("SELECT * FROM tbl_job_type")
    suspend fun getAll(): List<JobType>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: JobType)

    @Update
    suspend fun update(item: JobType)

    @Delete
    suspend fun delete(item: JobType)

    @Query("DELETE FROM tbl_job_type")
    suspend fun clearAll()
}