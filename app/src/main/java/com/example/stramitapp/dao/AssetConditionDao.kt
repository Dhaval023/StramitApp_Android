package com.example.stramitapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.stramitapp.model.AssetCondition

@Dao
interface AssetConditionDao {

    @Query("SELECT * FROM tbl_asset_condition WHERE id = :id")
    suspend fun getById(id: Int): AssetCondition?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(item: AssetCondition)

    @Update
    suspend fun update(item: AssetCondition): Int

    @Delete
    suspend fun delete(item: AssetCondition)

    @Query("SELECT * FROM tbl_asset_condition")
    suspend fun getAll(): List<AssetCondition>

    @Query("SELECT * FROM tbl_asset_condition WHERE company_id = :companyId AND update_flag != 'D'")
    suspend fun getByCompanyId(companyId: Int): List<AssetCondition>

    @Query("SELECT * FROM tbl_asset_condition WHERE company_id = :companyId LIMIT 1")
    suspend fun getFirstByCompanyId(companyId: Int): AssetCondition?

    @Query("DELETE FROM tbl_asset_condition")
    suspend fun clearAll()
}