package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.model.Rights

@Dao
interface RightsDao {

    @Query("SELECT * FROM tbl_rights WHERE rightsId = :id LIMIT 1")
    suspend fun getById(id: Int): Rights?

    @Query("SELECT * FROM tbl_rights")
    suspend fun getAll(): List<Rights>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Rights)

    @Update
    suspend fun update(item: Rights)

    @Delete
    suspend fun delete(item: Rights)

    @Query("DELETE FROM tbl_rights")
    suspend fun clearAll()
}