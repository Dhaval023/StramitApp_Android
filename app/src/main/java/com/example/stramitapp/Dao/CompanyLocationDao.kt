package com.example.stramitapp.Dao

import androidx.room.*
import com.example.stramitapp.model.CompanyLocation

@Dao
interface CompanyLocationDao {

    @Query("SELECT * FROM tbl_company_location WHERE Location_id = :id LIMIT 1")
    suspend fun getItem(id: Int): CompanyLocation?

    @Query("SELECT * FROM tbl_company_location WHERE company_id = :companyId AND update_flag != 'D'")
    suspend fun getItemByCompany(companyId: Int): List<CompanyLocation>

    @Query("""
        SELECT * FROM tbl_company_location
        WHERE company_id = :companyId 
        AND (Location_id = :locationId OR Parent_Location_id = :locationId)
    """)
    suspend fun getParentLocation(companyId: Int, locationId: Int): List<CompanyLocation>

    @Query("""
        SELECT * FROM tbl_company_location
        WHERE company_id = :companyId
        AND responsible_user_id = :userId
    """)
    suspend fun getItemByCompanyUser(companyId: Int, userId: Int): List<CompanyLocation>

    @Query("""
        SELECT * FROM tbl_company_location
        WHERE company_id = :companyId
        AND location_barcode = :barcode
        LIMIT 1
    """)
    suspend fun getLocationByScan(companyId: Int, barcode: String): CompanyLocation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CompanyLocation)

    @Update
    suspend fun update(item: CompanyLocation)

    @Delete
    suspend fun delete(item: CompanyLocation)

    @Query("SELECT * FROM tbl_company_location")
    suspend fun getAll(): List<CompanyLocation>

    @Query("SELECT * FROM tbl_company_location WHERE location_id = :locationId LIMIT 1")
    suspend fun getById(locationId: Int): CompanyLocation?
}