package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.model.UserRole

@Dao
interface UserRoleDao {

    @Query("SELECT * FROM tbl_user_role WHERE user_id = :id LIMIT 1")
    suspend fun getById(id: Int): UserRole?

    @Query("SELECT * FROM tbl_user_role")
    suspend fun getAll(): List<UserRole>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: UserRole)

    @Delete
    suspend fun delete(item: UserRole)

    @Query("DELETE FROM tbl_user_role")
    suspend fun clearAll()
}