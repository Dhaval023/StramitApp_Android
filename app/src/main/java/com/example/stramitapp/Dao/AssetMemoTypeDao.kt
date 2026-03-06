package com.example.stramitapp.Dao

import androidx.room.*
import com.example.stramitapp.model.AssetMemoType

@Dao
interface AssetMemoTypeDao {

    @Query("SELECT * FROM tbl_asset_memo_type WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): AssetMemoType?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AssetMemoType)

    @Update
    suspend fun update(item: AssetMemoType): Int

    @Delete
    suspend fun delete(item: AssetMemoType)

    @Query("SELECT * FROM tbl_asset_memo_type")
    suspend fun getAll(): List<AssetMemoType>

    @Query("DELETE FROM tbl_asset_memo_type")
    suspend fun clear()

    @Query("""
        SELECT * FROM tbl_asset_memo_type 
        WHERE last_update_date > :lastSyncUpData
    """)
    suspend fun getItemsToExport(lastSyncUpData: String): List<AssetMemoType>

    @Query("SELECT MAX(id) + 1 FROM tbl_asset_memo_type")
    suspend fun getNewId(): Int?
}