package com.example.stramitapp.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.stramitapp.model.AssetIssueInfo

@Dao
interface AssetIssueInfoDao {

    @Query("SELECT * FROM tbl_asset_issue_info WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): AssetIssueInfo?

    @Query("SELECT MAX(id) + 1 FROM tbl_asset_issue_info")
    suspend fun getNextId(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AssetIssueInfo): Long

    @Update
    suspend fun update(item: AssetIssueInfo): Int

    @Delete
    suspend fun delete(item: AssetIssueInfo)

    @Query("DELETE FROM tbl_asset_issue_info")
    suspend fun clearAll()

    @Query("SELECT * FROM tbl_asset_issue_info")
    suspend fun getAll(): List<AssetIssueInfo>

    @Query("""
        SELECT id, type, asset_id, device_id, date, issue_to, update_flag,
               last_update_date, flag_sync, updated_by, attribute_device_id,
               issue_to_supplierId, description, condition_id, name
        FROM tbl_asset_issue_info
        WHERE last_update_date > :lastSyncUpData
        AND updated_by = :userId
    """)
    suspend fun getItemsToExport(lastSyncUpData: String, userId: Int): List<AssetIssueInfo>
}