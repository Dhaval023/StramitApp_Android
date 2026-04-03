package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.model.Application

@Dao
interface ApplicationDataDao {


    @Query("SELECT * FROM tbl_application WHERE appId = :id LIMIT 1")
    suspend fun getById(id: Int): Application?

    @Query("SELECT * FROM tbl_application")
    suspend fun getAll(): List<Application>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Application)

    @Update
    suspend fun update(item: Application): Int

    @Delete
    suspend fun delete(item: Application)

    @Query("DELETE FROM tbl_application")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM tbl_application")
    suspend fun count(): Int

    @Query("SELECT MAX(appId) FROM tbl_application")
    suspend fun getMaxId(): Int?
}