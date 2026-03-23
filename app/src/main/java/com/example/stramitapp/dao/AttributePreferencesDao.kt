package com.example.stramitapp.dao

import androidx.room.*
import com.example.stramitapp.model.AttributePreferences
import com.example.stramitapp.model.MixAttributePreferences

@Dao
interface AttributePreferencesDao {

    @Query("SELECT * FROM tbl_attribute_preferences WHERE id = :id LIMIT 1")
    suspend fun getItem(id: Int): AttributePreferences?

    @Query("SELECT * FROM tbl_attribute_preferences")
    suspend fun getAll(): List<AttributePreferences>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AttributePreferences)

    @Update
    suspend fun update(item: AttributePreferences): Int

    @Delete
    suspend fun delete(item: AttributePreferences): Int

    @Query("DELETE FROM tbl_attribute_preferences")
    suspend fun clear()

    @Query("""
        SELECT * FROM tbl_attribute_preferences
        WHERE table_id = :tableId AND company_id = :companyId
    """)
    suspend fun getItemsByTableCompany(
        tableId: Int,
        companyId: Int
    ): List<AttributePreferences>

    @Query("""
        SELECT * FROM tbl_attribute_preferences
        WHERE table_id = :tableId
        AND company_id = :companyId
        AND attribute_id IN (:attributes)
    """)
    suspend fun getItemsByAttributes(
        tableId: Int,
        companyId: Int,
        attributes: List<Int>
    ): List<AttributePreferences>

    @Query("""
        SELECT 
            p.company_id as companyId,
            p.table_id as tableId,
            t.table_name as tableName,
            p.attribute_id as attributeId,
            a.column_name as attributeName,
            substr(p.visible_mandatory,1,1) as visible,
            substr(p.visible_mandatory,2,1) as required,
            p.custom_name as customName
        FROM tbl_attribute_preferences p
        INNER JOIN tbl_attribute a
            ON a.table_id = p.table_id
            AND a.id = p.attribute_id
        INNER JOIN tbl_attribute_table t
            ON t.id = a.table_id
        WHERE p.company_id = :companyId
        AND t.table_name = :tableName
        ORDER BY a.column_name
    """)
    suspend fun getAttributes(
        companyId: Int,
        tableName: String
    ): List<MixAttributePreferences>
}