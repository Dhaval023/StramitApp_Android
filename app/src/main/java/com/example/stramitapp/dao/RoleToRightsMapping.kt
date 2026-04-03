package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.model.RoleToRightsMapping

@Dao
interface RoleToRightsMappingDao {

    @Query("SELECT * FROM tbl_role_to_rights_mapping WHERE roleId = :id LIMIT 1")
    suspend fun getById(id: Int): RoleToRightsMapping?

    @Query("SELECT * FROM tbl_role_to_rights_mapping")
    suspend fun getAll(): List<RoleToRightsMapping>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RoleToRightsMapping)

    @Delete
    suspend fun delete(item: RoleToRightsMapping)

    @Query("DELETE FROM tbl_role_to_rights_mapping")
    suspend fun clearAll()
}