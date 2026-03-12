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
    WHERE update_flag != 'D'
    AND company_id = :companyId
    AND (:locationId = 0 OR location_id = :locationId)
    AND (:barcode = '' OR barcode = :barcode)
    ORDER BY asset_id DESC""")
    suspend fun searchAssets(companyId: Int,locationId: Int,barcode: String): List<Asset>

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
     * Returns ONLY assets created on this device (update_flag = 'I') since
     * the last upload.
     *
     * IMPORTANT: Assets downloaded from the server have update_flag = 'U'
     * and must NOT be sent back — the server returns HTTP 400 if you do.
     * The old query had no flag filter so it returned all 59k server assets
     * on every sync, causing OOM and HTTP 400.
     */
    @Query("""
        SELECT * FROM tbl_asset
        WHERE update_flag = 'I'
        AND (create_date > :lastSync OR last_update_date > :lastSync)
    """)
    suspend fun getItemsToExport(lastSync: String): List<Asset>

    /**
     * COUNT of device-created assets pending upload.
     * Cheap check — no rows loaded into memory.
     */
    @Query("""
        SELECT COUNT(*) FROM tbl_asset
        WHERE update_flag = 'I'
        AND (create_date > :lastSync OR last_update_date > :lastSync)
    """)
    suspend fun getItemsToExportCount(lastSync: String): Int

    /**
     * Returns assets with update_flag = 'I' (not yet confirmed by server).
     * Used by updateAssetsFlag after a successful upload.
     */
    @Query("""
        SELECT * FROM tbl_asset
        WHERE update_flag = 'I'
    """)
    suspend fun getItemsFlagI(): List<Asset>

    /**
     * COUNT of assets still flagged 'I'.
     * Used to drive the batched flag-update loop.
     */
    @Query("""
        SELECT COUNT(*) FROM tbl_asset
        WHERE update_flag = 'I'
    """)
    suspend fun getItemsFlagICount(): Int

    /**
     * Paginated fetch of 'I'-flagged assets.
     * Prevents loading all flag-update candidates into memory at once.
     */
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