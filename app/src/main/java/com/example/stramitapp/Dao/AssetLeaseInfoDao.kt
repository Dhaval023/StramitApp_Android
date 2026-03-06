package com.example.stramitapp.Dao

import androidx.room.*
import com.example.stramitapp.model.AssetLeaseInfo

@Dao
interface AssetLeaseInfoDao {

    @Query("SELECT * FROM tbl_asset_lease_info WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): AssetLeaseInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AssetLeaseInfo)

    @Update
    suspend fun update(item: AssetLeaseInfo): Int

    @Delete
    suspend fun delete(item: AssetLeaseInfo)

    @Query("SELECT * FROM tbl_asset_lease_info")
    suspend fun getAll(): List<AssetLeaseInfo>

    @Query("DELETE FROM tbl_asset_lease_info")
    suspend fun clear()

    @Query(
        """
        SELECT * FROM tbl_asset_lease_info 
        WHERE last_update_date > :lastSyncUpData
        """
    )
    suspend fun getItemsToExport(lastSyncUpData: String): List<AssetLeaseInfo>
}