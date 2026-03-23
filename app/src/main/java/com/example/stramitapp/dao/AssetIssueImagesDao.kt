package com.example.stramitapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.stramitapp.model.AssetIssueImages
@Dao
interface AssetIssueImagesDao {

    @Query("SELECT MAX(id) + 1 FROM tbl_asset_issue_images")
    suspend fun getNextId(): Int?

    @Query("SELECT * FROM tbl_asset_issue_images WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): AssetIssueImages?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AssetIssueImages)

    @Update
    suspend fun update(item: AssetIssueImages): Int

    @Delete
    suspend fun delete(item: AssetIssueImages)

    @Query("DELETE FROM tbl_asset_issue_images")
    suspend fun clearAll()

    @Query("SELECT * FROM tbl_asset_issue_images")
    suspend fun getAll(): List<AssetIssueImages>

    @Query("SELECT * FROM tbl_asset_issue_images WHERE last_update_date > :lastSyncUpData")
    suspend fun getItemsToExport(lastSyncUpData: String): List<AssetIssueImages>
}