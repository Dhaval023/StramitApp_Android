package com.example.stramitapp.dao


import androidx.room.*
import com.example.stramitapp.model.DeviceType

@Dao
interface DeviceTypeDao {

    @Query("SELECT * FROM tbl_device_type WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): DeviceType?

    @Query("SELECT * FROM tbl_device_type")
    suspend fun getAll(): List<DeviceType>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: DeviceType)

    @Update
    suspend fun update(item: DeviceType)

    @Delete
    suspend fun delete(item: DeviceType)

    @Query("DELETE FROM tbl_device_type")
    suspend fun clearAll()
}