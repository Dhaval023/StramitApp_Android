package com.example.stramitapp.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.stramitapp.model.Company

@Dao
interface CompanyDao {


    @Query("SELECT * FROM tbl_company WHERE company_id = :id LIMIT 1")
    suspend fun getById(id: Int): Company?

    @Query("SELECT * FROM tbl_company LIMIT 1")
    suspend fun getFirst(): Company?

    @Query("SELECT * FROM tbl_company")
    suspend fun getAll(): List<Company>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(company: Company)

    @Update
    suspend fun update(company: Company)

    @Delete
    suspend fun delete(company: Company)

    @Query("DELETE FROM tbl_company WHERE company_id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM tbl_company")
    suspend fun deleteAll(): Int
}