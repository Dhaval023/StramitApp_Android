package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.model.Settings

@Dao
interface SettingsDao {

    @Query("SELECT * FROM tbl_setting WHERE `key` = :key LIMIT 1")
    suspend fun getItem(key: String): Settings?

    @Query("SELECT * FROM tbl_setting")
    suspend fun getAll(): List<Settings>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Settings): Long

    @Update
    suspend fun update(item: Settings): Int

    @Delete
    suspend fun delete(item: Settings): Int
}