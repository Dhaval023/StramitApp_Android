package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.model.ReaderDevice

@Dao
interface ReaderDeviceDao {

    @Query("SELECT * FROM tbl_reader WHERE id = :id LIMIT 1")
    suspend fun getItem(id: Int): ReaderDevice?

    @Query("SELECT * FROM tbl_reader")
    suspend fun getAll(): List<ReaderDevice>

    @Query("SELECT * FROM tbl_reader WHERE id = :idTable")
    suspend fun getByTable(idTable: Int): List<ReaderDevice>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ReaderDevice)

    @Update
    suspend fun update(item: ReaderDevice): Int

    @Delete
    suspend fun delete(item: ReaderDevice): Int
}