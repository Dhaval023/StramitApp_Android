package com.example.stramitapp.Dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.stramitapp.model.AssetInspectionInfo

@Dao
interface AssetInspectionInfoDao {

    @Query("SELECT * FROM tbl_asset_inspection_info WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): AssetInspectionInfo?

    @Insert
    suspend fun insert(item: AssetInspectionInfo)

    @Update
    suspend fun update(item: AssetInspectionInfo): Int

    @Delete
    suspend fun delete(item: AssetInspectionInfo)

    @Query("DELETE FROM tbl_asset_inspection_info")
    suspend fun clearAll()

    @Query("SELECT * FROM tbl_asset_inspection_info")
    suspend fun getAll(): List<AssetInspectionInfo>

    @Query("SELECT * FROM tbl_asset_inspection_info WHERE last_update_date > :lastSyncUpData")
    suspend fun getItemsToExport(lastSyncUpData: String): List<AssetInspectionInfo>
}