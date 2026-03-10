package com.example.stramitapp.Dao

import androidx.room.*
import com.example.stramitapp.model.AssetStatus


@Dao
interface AssetStatusDao {

    @Query("SELECT * FROM tbl_asset_status WHERE id = :id LIMIT 1")
    suspend fun getItem(id: Int): AssetStatus?

    @Query("SELECT * FROM tbl_asset_status")
    suspend fun getAll(): List<AssetStatus>

    @Query("SELECT * FROM tbl_asset_status WHERE company_id = :companyId")
    suspend fun getItemsByCompany(companyId: Int): List<AssetStatus>

    @Query("SELECT * FROM tbl_asset_status WHERE company_id = :companyId LIMIT 1")
    suspend fun getFirstItemByCompany(companyId: Int): AssetStatus?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AssetStatus)

    @Update
    suspend fun update(item: AssetStatus)

    @Delete
    suspend fun delete(item: AssetStatus)

    @Query("DELETE FROM tbl_asset_status")
    suspend fun clear()
}