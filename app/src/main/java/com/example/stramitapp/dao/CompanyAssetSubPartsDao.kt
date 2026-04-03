package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.model.CompanyAssetSubParts

@Dao
interface CompanyAssetSubPartsDao {

    @Query("SELECT * FROM tbl_company_asset_sub_parts WHERE sub_part_id = :id LIMIT 1")
    suspend fun getById(id: Int): CompanyAssetSubParts?

    @Query("SELECT * FROM tbl_company_asset_sub_parts")
    suspend fun getAll(): List<CompanyAssetSubParts>

    @Query("""
        SELECT * FROM tbl_company_asset_sub_parts 
        WHERE part_id = :typeId AND update_flag != 'D'
    """)
    suspend fun getByType(typeId: Int): List<CompanyAssetSubParts>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CompanyAssetSubParts)

    @Update
    suspend fun update(item: CompanyAssetSubParts)

    @Delete
    suspend fun delete(item: CompanyAssetSubParts)

    @Query("DELETE FROM tbl_company_asset_sub_parts")
    suspend fun clearAll()
}