package com.example.stramitapp.dao

import com.example.stramitapp.model.Licensee
import androidx.room.*

@Dao
interface LicenseeDao {

    @Query("SELECT * FROM tbl_licensee WHERE licensee_id = :id LIMIT 1")
    suspend fun getById(id: Int): Licensee?

    @Query("SELECT * FROM tbl_licensee")
    suspend fun getAll(): List<Licensee>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Licensee)

    @Update
    suspend fun update(item: Licensee)

    @Delete
    suspend fun delete(item: Licensee)

    @Query("DELETE FROM tbl_licensee")
    suspend fun clearAll()
}