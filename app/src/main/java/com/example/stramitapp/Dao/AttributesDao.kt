package com.example.stramitapp.Dao

import androidx.room.*
import com.example.stramitapp.model.Attributes

@Dao
interface AttributesDao {

    @Query("SELECT * FROM tbl_attribute WHERE id = :id LIMIT 1")
    suspend fun getItem(id: Int): Attributes?

    @Query("SELECT * FROM tbl_attribute")
    suspend fun getAll(): List<Attributes>

    @Query("SELECT * FROM tbl_attribute WHERE table_id = :tableId")
    suspend fun getItemsByTable(tableId: Int): List<Attributes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Attributes)

    @Update
    suspend fun update(item: Attributes): Int

    @Delete
    suspend fun delete(item: Attributes): Int

    @Query("DELETE FROM tbl_attribute")
    suspend fun clear()
}