package com.example.stramitapp.Dao

import androidx.room.*
import com.example.stramitapp.model.AssetMaintenanceInfo

@Dao
interface AssetMaintenanceInfoDao {

    @Query("SELECT * FROM tbl_asset_maintenance_info WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): AssetMaintenanceInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AssetMaintenanceInfo)

    @Update
    suspend fun update(item: AssetMaintenanceInfo): Int

    @Delete
    suspend fun delete(item: AssetMaintenanceInfo)

    @Query("SELECT * FROM tbl_asset_maintenance_info")
    suspend fun getAll(): List<AssetMaintenanceInfo>

    @Query("DELETE FROM tbl_asset_maintenance_info")
    suspend fun clear()

    @Query(
        """
        SELECT * FROM tbl_asset_maintenance_info 
        WHERE last_update_date > :lastSyncUpData
        """
    )
    suspend fun getItemsToExport(lastSyncUpData: String): List<AssetMaintenanceInfo>
}