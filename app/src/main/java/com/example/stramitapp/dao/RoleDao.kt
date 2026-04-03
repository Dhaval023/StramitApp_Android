package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.model.Role

@Dao
interface RoleDao {

    @Query("SELECT * FROM tbl_role WHERE roleId = :id LIMIT 1")
    suspend fun getById(id: Int): Role?

    @Query("SELECT * FROM tbl_role")
    suspend fun getAll(): List<Role>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Role)

    @Update
    suspend fun update(item: Role)

    @Delete
    suspend fun delete(item: Role)

    @Query("DELETE FROM tbl_role")
    suspend fun clearAll()
}