package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.models.JobDescription

@Dao
interface JobDescriptionDao {

    @Query("SELECT * FROM tbl_job_description WHERE job_desc = :id LIMIT 1")
    suspend fun getById(id: Int): JobDescription?

    @Query("SELECT * FROM tbl_job_description WHERE job_desc = :desc LIMIT 1")
    suspend fun getByDesc(desc: String): JobDescription?

    @Query("SELECT * FROM tbl_job_description")
    suspend fun getAll(): List<JobDescription>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: JobDescription)

    @Update
    suspend fun update(item: JobDescription)

    @Delete
    suspend fun delete(item: JobDescription)

    @Query("DELETE FROM tbl_job_description")
    suspend fun clearAll()
}