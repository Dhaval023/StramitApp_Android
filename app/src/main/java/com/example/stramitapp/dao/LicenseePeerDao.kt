package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.model.LicenseePeer

@Dao
interface LicenseePeerDao {

    @Query("SELECT * FROM tbl_licensee_peer WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): LicenseePeer?

    @Query("SELECT * FROM tbl_licensee_peer")
    suspend fun getAll(): List<LicenseePeer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: LicenseePeer)

    @Update
    suspend fun update(item: LicenseePeer)

    @Delete
    suspend fun delete(item: LicenseePeer)

    @Query("DELETE FROM tbl_licensee_peer")
    suspend fun clearAll()
}