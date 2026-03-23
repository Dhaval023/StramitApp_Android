package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.model.AssetMemoInfo

@Dao
interface AssetMemoInfoDao {

    @Query("SELECT * FROM tbl_asset_memo_info WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): AssetMemoInfo?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AssetMemoInfo)

    @Update
    suspend fun update(item: AssetMemoInfo): Int

    @Delete
    suspend fun delete(item: AssetMemoInfo)


    @Query("SELECT * FROM tbl_asset_memo_info")
    suspend fun getAll(): List<AssetMemoInfo>


    @Query("DELETE FROM tbl_asset_memo_info")
    suspend fun clear()

    @Query("""
        SELECT * FROM tbl_asset_memo_info 
        WHERE last_update_date > :lastSyncUpData
    """)
    suspend fun getItemsToExport(lastSyncUpData: String): List<AssetMemoInfo>

    @Query("SELECT MAX(id) + 1 FROM tbl_asset_memo_info")
    suspend fun getNewId(): Int?
}