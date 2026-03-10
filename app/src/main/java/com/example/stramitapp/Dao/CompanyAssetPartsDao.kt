package com.example.stramitapp.Dao

import androidx.room.*
import com.example.stramitapp.model.CompanyAssetParts

@Dao
interface CompanyAssetPartsDao {

    @Query("SELECT * FROM tbl_company_asset_parts WHERE part_id = :partId LIMIT 1")
    suspend fun getItem(partId: Int): CompanyAssetParts?

    @Query("SELECT * FROM tbl_company_asset_parts")
    suspend fun getAll(): List<CompanyAssetParts>

    @Query("SELECT * FROM tbl_company_asset_parts WHERE company_id = :companyId")
    suspend fun getItemsByCompany(companyId: Int): List<CompanyAssetParts>

    @Query("""
        SELECT * FROM tbl_company_asset_parts
        WHERE parent_part_id = :deviceId
        AND update_flag != 'D'
    """)
    suspend fun getItemsByDevice(deviceId: Int): List<CompanyAssetParts>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CompanyAssetParts)

    @Update
    suspend fun update(item: CompanyAssetParts): Int

    @Delete
    suspend fun delete(item: CompanyAssetParts): Int

    @Query("DELETE FROM tbl_company_asset_parts")
    suspend fun clearAll(): Int
}