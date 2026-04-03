package com.example.stramitapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.stramitapp.model.Asset
import com.example.stramitapp.model.Shipment

@Dao
interface AssetDao {

    @Query("SELECT * FROM tbl_asset WHERE asset_id = :id LIMIT 1")
    suspend fun getById(id: Int): Asset?

    @Query("SELECT MAX(asset_id) FROM tbl_asset")
    suspend fun getMaxAssetId(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Asset)

    @Update
    suspend fun update(item: Asset): Int

    @Delete
    suspend fun delete(item: Asset)

    @Query("SELECT COUNT(*) FROM tbl_asset")
    suspend fun count(): Int

    @Query("DELETE FROM tbl_asset")
    suspend fun clearAll()

    @Query("""
        SELECT * FROM tbl_asset 
        WHERE company_id = :companyId 
        AND barcode = :barcode 
        LIMIT 1
    """)
    suspend fun getByBarcode(companyId: Int?, barcode: String): Asset?

    @Query("""
    SELECT * FROM tbl_asset
    WHERE update_flag != 'D'
    AND company_id = :companyId
    AND (:locationId = 0 OR location_id = :locationId)
    AND (:barcode = '' OR barcode = :barcode)
    ORDER BY asset_id DESC
    LIMIT 100
     """)
    suspend fun searchAssets(companyId: Int, locationId: Int, barcode: String): List<Asset>

    @Query("""
        SELECT * FROM tbl_asset 
        WHERE company_id = :companyId 
        AND UPPER(tag) = UPPER(:tag) 
        LIMIT 1
    """)
    suspend fun getByTag(companyId: Int?, tag: String): Asset?

    @Query("""
        SELECT * FROM tbl_asset
        WHERE company_id = :companyId
        AND (tag = :barcodeTag OR barcode = :barcodeTag)
        LIMIT 1
    """)
    suspend fun getByTagOrBarcode(companyId: Int, barcodeTag: String): Asset?

    @Query("SELECT * FROM tbl_asset")
    suspend fun getAll(): List<Asset>

    @Query("""
        SELECT * FROM tbl_asset 
        WHERE update_flag != 'D'
    """)
    suspend fun getActiveAssets(): List<Asset>

    @Query("""
        SELECT * FROM tbl_asset 
        WHERE custom_18 = :shipmentNumber 
        AND update_flag != 'D'
    """)
    suspend fun getShipmentAssets(shipmentNumber: String): List<Asset>

    @Query("""
        DELETE FROM tbl_asset 
        WHERE custom_18 IN 
        (SELECT shipment_number FROM tbl_shipment WHERE update_flag = 'D')
    """)
    suspend fun removeDeactivatedShipmentAssets(): Int

    @Query("""
        DELETE FROM tbl_asset 
        WHERE DATE(purchase_date) BETWEEN DATE(:fromDate) AND DATE(:toDate)
    """)
    suspend fun deleteAssetsBetween(fromDate: String, toDate: String): Int

    @Query("""
        SELECT * FROM tbl_asset
        WHERE update_flag != 'D'
        ORDER BY asset_id DESC
        LIMIT 100
    """)
    suspend fun getRecentAssets(): List<Asset>

    @Query("""
        SELECT * FROM tbl_asset
        WHERE update_flag = 'I'
        AND (create_date > :lastSync OR last_update_date > :lastSync)
    """)
    suspend fun getItemsToExport(lastSync: String): List<Asset>

    @Query("""
        SELECT COUNT(*) FROM tbl_asset
        WHERE update_flag = 'I'
        AND (create_date > :lastSync OR last_update_date > :lastSync)
    """)
    suspend fun getItemsToExportCount(lastSync: String): Int

    @Query("""
        SELECT * FROM tbl_asset
        WHERE update_flag = 'I'
    """)
    suspend fun getItemsFlagI(): List<Asset>

    @Query("""
        SELECT COUNT(*) FROM tbl_asset
        WHERE update_flag = 'I'
    """)
    suspend fun getItemsFlagICount(): Int

    @Query("""
        SELECT * FROM tbl_asset
        WHERE update_flag = 'I'
        ORDER BY asset_id ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getItemsFlagIPaged(limit: Int, offset: Int): List<Asset>

    @Query("""
        SELECT * FROM tbl_asset
        WHERE image_sync = 1
    """)
    suspend fun getAssetsForImageSync(): List<Asset>

    @Query("SELECT * FROM tbl_asset WHERE update_flag = :assetUpdateFlag")
    suspend fun getByUpdateFlag(assetUpdateFlag: String): List<Asset>

    @Query("SELECT * FROM tbl_shipment WHERE update_flag != 'D'")
    suspend fun getShipments(): List<Shipment>

    @Query("""
        SELECT * FROM tbl_shipment 
        WHERE shipment_number = :shipmentNumber 
        LIMIT 1
    """)
    suspend fun getShipment(shipmentNumber: String): Shipment?

    @Query("""
    SELECT * FROM tbl_asset
    WHERE update_flag != 'D'
    AND (:shipmentNumber = '' OR custom_18 LIKE '%' || :shipmentNumber || '%')
    AND (:m3CO = ''           OR custom_13 LIKE '%' || :m3CO           || '%')
    AND (:m3DO = ''           OR custom_22 LIKE '%' || :m3DO           || '%')
    ORDER BY asset_id DESC
""")
    suspend fun searchShipmentAssets(
        shipmentNumber: String,
        m3CO: String,
        m3DO: String
    ): List<Asset>

    @Query("""
    SELECT * FROM tbl_asset 
    WHERE company_id = :companyId
    AND barcode = :barcode 
    AND custom_18 = :shipmentNumber
    AND update_flag != 'D'
    LIMIT 1
""")
    suspend fun getShipmentItemByBarcodeOnly(
        companyId: Int,
        barcode: String,
        shipmentNumber: String
    ): Asset?

    @Query("""
    SELECT * FROM tbl_asset 
    WHERE company_id = :companyId
    AND UPPER(tag) = UPPER(:tag)
    AND custom_18 = :shipmentNumber
    AND update_flag != 'D'
    LIMIT 1
""")
    suspend fun getShipmentItemByTagOnly(
        companyId: Int,
        tag: String,
        shipmentNumber: String
    ): Asset?

    @Query("""
        SELECT * FROM tbl_asset
        WHERE update_flag != 'D'
          AND update_flag != 'S'
          AND (:shipmentNumber = '' OR custom_18 = :shipmentNumber)
        ORDER BY asset_id DESC
    """)
    suspend fun getShipmentResultAssets(shipmentNumber: String): List<Asset>
}