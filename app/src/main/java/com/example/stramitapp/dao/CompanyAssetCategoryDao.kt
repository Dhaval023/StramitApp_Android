package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.model.CompanyAssetCategory

@Dao
interface CompanyAssetCategoryDao {

    @Query("SELECT * FROM tbl_company_asset_category WHERE company_id = :companyId")
    suspend fun getItemsByCompany(companyId: Int): List<CompanyAssetCategory>

    @Query("SELECT * FROM tbl_company_asset_category WHERE id = :id LIMIT 1")
    suspend fun getItem(id: Int): CompanyAssetCategory?

    @Query("SELECT * FROM tbl_company_asset_category")
    suspend fun getAll(): List<CompanyAssetCategory>

    @Query("SELECT * FROM tbl_company_asset_category WHERE company_id = :companyId LIMIT 1")
    suspend fun getFirstItemByCompany(companyId: Int): CompanyAssetCategory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CompanyAssetCategory)

    @Update
    suspend fun update(item: CompanyAssetCategory): Int

    @Delete
    suspend fun delete(item: CompanyAssetCategory): Int

    @Query("DELETE FROM tbl_company_asset_category")
    suspend fun clearAll(): Int
}