package com.example.stramitapp.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.stramitapp.model.AssetFinancialInfo

@Dao
interface AssetFinancialInfoDao {

    // ---------------- BASIC CRUD ----------------

    @Query("SELECT * FROM tbl_asset_financial_info WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): AssetFinancialInfo?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(item: AssetFinancialInfo)

    @Update
    suspend fun update(item: AssetFinancialInfo): Int

    @Delete
    suspend fun delete(item: AssetFinancialInfo)

    @Query("SELECT * FROM tbl_asset_financial_info")
    suspend fun getAll(): List<AssetFinancialInfo>

    @Query("DELETE FROM tbl_asset_financial_info")
    suspend fun clearAll()

    // ---------------- EXPORT ----------------

    @Query("""
        SELECT * FROM tbl_asset_financial_info 
        WHERE last_update_date > :lastSyncUpData
    """)
    suspend fun getItemsToExport(lastSyncUpData: String): List<AssetFinancialInfo>
}