package com.example.stramitapp.Dao

import androidx.room.*
import com.example.stramitapp.model.JobAssignment


@Dao
interface JobAssignmentDao {

    @Query("SELECT * FROM tbl_job_assignment WHERE job_id = :jobId AND assign_to = :assignTo LIMIT 1")
    suspend fun getItem(jobId: Int, assignTo: Int): JobAssignment?

    @Query("SELECT * FROM tbl_job_assignment WHERE assign_to = :assignTo")
    suspend fun getByUser(assignTo: Int): List<JobAssignment>

    @Query("SELECT * FROM tbl_job_assignment WHERE assign_to = :assignTo AND status = :status")
    suspend fun getCompleted(assignTo: Int, status: Int): List<JobAssignment>

    @Query("SELECT * FROM tbl_job_assignment")
    suspend fun getAll(): List<JobAssignment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: JobAssignment)

    @Delete
    suspend fun delete(item: JobAssignment)

    @Query("SELECT * FROM tbl_job_assignment WHERE last_update_date > :lastSyncUpData")
    suspend fun getItemsToExport(lastSyncUpData: String): List<JobAssignment>
}