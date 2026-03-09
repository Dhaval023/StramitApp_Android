package com.example.stramitapp.Dao

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

    // ---------------- BASIC CRUD ----------------

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

    // ---------------- BARCODE / TAG ----------------

    @Query("""
        SELECT * FROM tbl_asset 
        WHERE company_id = :companyId 
        AND barcode = :barcode 
        LIMIT 1
    """)
    suspend fun getByBarcode(companyId: Int?, barcode: String): Asset?

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

    // ---------------- BULK FETCH ----------------

    @Query("SELECT * FROM tbl_asset")
    suspend fun getAll(): List<Asset>

    @Query("""
        SELECT * FROM tbl_asset 
        WHERE update_flag != 'D'
    """)
    suspend fun getActiveAssets(): List<Asset>

    // ---------------- SHIPMENT ----------------

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

    // ---------------- SEARCH ----------------

    @Query("""
        SELECT * FROM tbl_asset
        WHERE update_flag != 'D'
        ORDER BY asset_id DESC
        LIMIT 100
    """)
    suspend fun getRecentAssets(): List<Asset>

    // ---------------- SYNC ----------------

    /**
     * Returns assets changed since the last sync upload.
     * Mirrors Xamarin: AssetDataStore.GetItemsToExportAsync(lastSyncUpData)
     *
     * Checks BOTH create_date and last_update_date so newly created
     * AND recently updated assets are included.
     */
    @Query("""
        SELECT * FROM tbl_asset
        WHERE create_date > :lastSync
           OR last_update_date > :lastSync
    """)
    suspend fun getItemsToExport(lastSync: String): List<Asset>

    /**
     * Returns assets with update_flag = 'I' (Insert — not yet confirmed by server).
     * Called with NO parameters — mirrors Xamarin: GetItemsFlagI() with no date filter.
     *
     * Fixed: removed the wrong :lastSync parameter that was previously here.
     */
    @Query("""
        SELECT * FROM tbl_asset
        WHERE update_flag = 'I'
    """)
    suspend fun getItemsFlagI(): List<Asset>

    @Query("""
        SELECT * FROM tbl_asset
        WHERE image_sync = 1
    """)
    suspend fun getAssetsForImageSync(): List<Asset>

    @Query("SELECT * FROM tbl_asset WHERE update_flag = :assetUpdateFlag")
    suspend fun getByUpdateFlag(assetUpdateFlag: String): List<Asset>

    // ---------------- SHIPMENT TABLE ----------------

    @Query("SELECT * FROM tbl_shipment WHERE update_flag != 'D'")
    suspend fun getShipments(): List<Shipment>

    @Query("""
        SELECT * FROM tbl_shipment 
        WHERE shipment_number = :shipmentNumber 
        LIMIT 1
    """)
    suspend fun getShipment(shipmentNumber: String): Shipment?
}