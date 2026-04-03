package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.models.LicenseeConfig

@Dao
interface LicenseeConfigDao {

    @Query("SELECT * FROM tbl_licensee_config WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): LicenseeConfig?

    @Query("SELECT * FROM tbl_licensee_config")
    suspend fun getAll(): List<LicenseeConfig>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: LicenseeConfig)

    @Update
    suspend fun update(item: LicenseeConfig)

    @Delete
    suspend fun delete(item: LicenseeConfig)

    @Query("DELETE FROM tbl_licensee_config")
    suspend fun clearAll()
}