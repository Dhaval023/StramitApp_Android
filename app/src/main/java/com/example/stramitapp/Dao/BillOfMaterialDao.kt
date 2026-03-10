package com.example.stramitapp.Dao

import androidx.room.*
import com.example.stramitapp.model.BillOfMaterial

@Dao
interface BillOfMaterialDao {

    @Query("SELECT * FROM tbl_bill_of_material WHERE id = :id LIMIT 1")
    suspend fun getItem(id: Int): BillOfMaterial?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: BillOfMaterial)

    @Update
    suspend fun update(item: BillOfMaterial): Int

    @Delete
    suspend fun delete(item: BillOfMaterial): Int

    @Query("SELECT * FROM tbl_bill_of_material")
    suspend fun getAll(): List<BillOfMaterial>

    @Query("""
        SELECT * FROM tbl_bill_of_material 
        WHERE last_update_date > :lastSyncUpData
    """)
    suspend fun getItemsToExport(lastSyncUpData: String): List<BillOfMaterial>
}