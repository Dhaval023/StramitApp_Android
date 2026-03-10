package com.example.stramitapp.Dao

import androidx.room.*
import com.example.stramitapp.model.AttributeTable

@Dao
interface AttributeTableDao {

    @Query("SELECT * FROM tbl_attribute_table WHERE id = :id LIMIT 1")
    suspend fun getItem(id: Int): AttributeTable?

    @Query("SELECT * FROM tbl_attribute_table WHERE table_name = :tableName LIMIT 1")
    suspend fun getItemByName(tableName: String): AttributeTable?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AttributeTable)

    @Update
    suspend fun update(item: AttributeTable): Int

    @Delete
    suspend fun delete(item: AttributeTable): Int

    @Query("SELECT * FROM tbl_attribute_table")
    suspend fun getAll(): List<AttributeTable>
}