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
    fun getById(id: Int): Company?

    @Query("SELECT * FROM tbl_company LIMIT 1")
    fun getFirst(): Company?

    @Query("SELECT * FROM tbl_company")
    fun getAll(): List<Company>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insert(company: Company)

    @Update
    fun update(company: Company)

    @Delete
    fun delete(company: Company)

    @Query("DELETE FROM tbl_company WHERE company_id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM tbl_company")
    fun deleteAll(): Int
}