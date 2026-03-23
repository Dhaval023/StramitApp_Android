package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.model.CompanyAssetType

@Dao
interface CompanyAssetTypeDao {

    @Query("SELECT * FROM tbl_company_asset_type WHERE id = :id LIMIT 1")
    suspend fun getItem(id: Int): CompanyAssetType?

    @Query("SELECT * FROM tbl_company_asset_type")
    suspend fun getAll(): List<CompanyAssetType>

    @Query("SELECT * FROM tbl_company_asset_type WHERE company_id = :companyId")
    suspend fun getItemsByCompany(companyId: Int): List<CompanyAssetType>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CompanyAssetType)

    @Update
    suspend fun update(item: CompanyAssetType): Int

    @Delete
    suspend fun delete(item: CompanyAssetType): Int

    @Query("DELETE FROM tbl_company_asset_type")
    suspend fun clearAll(): Int
}