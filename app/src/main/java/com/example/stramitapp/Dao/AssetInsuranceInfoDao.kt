package com.example.stramitapp.Dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.stramitapp.model.AssetInsuranceInfo
@Dao
interface AssetInsuranceInfoDao {

    @Query("SELECT * FROM tbl_asset_insurance_info WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): AssetInsuranceInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AssetInsuranceInfo)

    @Update
    suspend fun update(item: AssetInsuranceInfo): Int

    @Delete
    suspend fun delete(item: AssetInsuranceInfo)

    @Query("DELETE FROM tbl_asset_insurance_info")
    suspend fun clearAll()

    @Query("SELECT * FROM tbl_asset_insurance_info")
    suspend fun getAll(): List<AssetInsuranceInfo>

    @Query("SELECT * FROM tbl_asset_insurance_info WHERE last_update_date > :lastSyncUpData")
    suspend fun getItemsToExport(lastSyncUpData: String): List<AssetInsuranceInfo>
}