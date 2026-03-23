package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.model.AssetMovementInfo

@Dao
interface AssetMovementInfoDao {

    @Query("SELECT * FROM tbl_asset_movement_info WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): AssetMovementInfo?

    @Query("SELECT * FROM tbl_asset_movement_info")
    suspend fun getAll(): List<AssetMovementInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AssetMovementInfo)

    @Update
    suspend fun update(item: AssetMovementInfo)

    @Delete
    suspend fun delete(item: AssetMovementInfo)

    @Query("DELETE FROM tbl_asset_movement_info")
    suspend fun deleteAll()

    @Query("SELECT * FROM tbl_asset_movement_info WHERE last_update_date > :lastSyncUpData")
    suspend fun getItemsToExport(lastSyncUpData: String): List<AssetMovementInfo>
}