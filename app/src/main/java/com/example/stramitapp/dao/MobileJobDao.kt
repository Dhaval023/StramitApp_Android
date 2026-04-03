package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.models.MobileJob

@Dao
interface MobileJobDao {

    @Query("SELECT * FROM tbl_mobile_job WHERE job_id = :id LIMIT 1")
    suspend fun getById(id: Int): MobileJob?

    @Query("SELECT * FROM tbl_mobile_job")
    suspend fun getAll(): List<MobileJob>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MobileJob)

    @Update
    suspend fun update(item: MobileJob)

    @Delete
    suspend fun delete(item: MobileJob)

    @Query("DELETE FROM tbl_mobile_job")
    suspend fun clearAll()
}