package com.example.stramitapp.Dao

import androidx.room.*
import com.example.stramitapp.models.WpCompany

@Dao
interface WpCompanyDao {

    @Query("SELECT * FROM tbl_wp_company WHERE company_wp_id = :id LIMIT 1")
    suspend fun getItem(id: Int): WpCompany?

    @Query("SELECT * FROM tbl_wp_company")
    suspend fun getAll(): List<WpCompany>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WpCompany)

    @Update
    suspend fun update(item: WpCompany)

    @Delete
    suspend fun delete(item: WpCompany)

    @Query("DELETE FROM tbl_wp_company")
    suspend fun clear()

}