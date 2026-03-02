package com.example.stramitapp.Repositories

import android.util.Log
import com.example.stramitapp.model.Asset
import com.example.stramitapp.model.CompanyLocation
import com.example.stramitapp.model.Shipment
//import com.example.stramitapp.Repositories
//import com.example.stramitapp.Utilities.AppSettings
import java.time.LocalDate
import java.time.format.DateTimeFormatter
//
//class AssetDataStore : BaseRepository<Asset>(), IDataStore<Asset> {
//
//    suspend fun getItemAsync(id: Int): Asset? {
//        throw NotImplementedError("getItemAsync(id) is not implemented")
//    }
//
//    suspend fun getNewIdAsync(): Int {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<Asset>().maxOfOrNull { asset -> asset.assetId + 1 } ?: 0
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemAsync(idAsset: Int, deviceId: Int): Asset? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<Asset>()
//                    .firstOrNull { asset -> asset.assetId == idAsset && asset.deviceId == deviceId }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getShipmentItemByBarcodeAsync(companyId: Int?, barcode: String, shipmentNumber: String): Asset? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<Asset>()
//                    .firstOrNull { asset ->
//                        asset.companyId == companyId &&
//                                asset.barcode == barcode &&
//                                asset.custom18 == shipmentNumber &&
//                                asset.updateFlag != "D"
//                    }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemByBarcodeAsync(companyId: Int?, barcode: String): Asset? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<Asset>()
//                    .firstOrNull { asset -> asset.companyId == companyId && asset.barcode == barcode }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemByTagAsync(companyId: Int?, tag: String): Asset? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<Asset>()
//                    .firstOrNull { asset ->
//                        asset.companyId == companyId &&
//                                asset.tag.uppercase() == tag.uppercase()
//                    }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getShipmentItemByTagAsync(companyId: Int?, tag: String, shipmentNumber: String): Asset? {
//        return try {
//            println("Add Shipment Tag $tag $shipmentNumber")
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<Asset>()
//                    .firstOrNull { asset ->
//                        asset.companyId == companyId &&
//                                asset.tag.uppercase() == tag.uppercase() &&
//                                asset.custom18 == shipmentNumber &&
//                                asset.updateFlag != "D"
//                    }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: Asset): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                item.assetId = getMaxAssetId() + 1
//                it.insert(item)
//            }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun updateItemAsync(item: Asset): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.insertOrReplace(item)
//            }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun deleteItemAsync(item: Asset): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.delete(item)
//            }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun clearAsync(): Boolean {
//        throw NotImplementedError("clearAsync is not implemented")
//    }
//
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<Asset> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.rawQuery("SELECT * FROM tbl_asset WHERE update_flag != 'D'")
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getSearchItemsAsync(companyId: Int, locationId: Int, barcode: String): List<Asset> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                var query = "SELECT * FROM tbl_asset WHERE update_flag != 'D' AND company_id=$companyId"
//                if (locationId > 0) query += " AND location_id=$locationId"
//                if (barcode.isNotEmpty()) query += " AND barcode='$barcode'"
//                query += " ORDER BY asset_id DESC LIMIT 100"
//                it.rawQuery(query)
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    // Added 18/03/2025 for Search Shipment
//    suspend fun getSearchShipmentItemsAsync(shipmentNumber: String, m3co: String, m3do: String): List<Asset> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                var query = "SELECT * FROM tbl_asset WHERE update_flag != 'D'"
//                if (shipmentNumber.isNotEmpty()) query += " AND custom_18='$shipmentNumber'"
//                if (m3co.isNotEmpty()) query += " AND TRIM(custom_13)='$m3co'"
//                if (m3do.isNotEmpty()) query += " AND TRIM(custom_22)='$m3do'"
//                query += " ORDER BY asset_id DESC"
//                it.rawQuery(query)
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    // Added 18/03/2025 for Search Shipment
//    suspend fun getSearchShipmentListItemsAsync(shipmentNumber: String): List<Asset> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                var query = "SELECT * FROM tbl_asset WHERE update_flag != 'D' AND update_flag != 'S'"
//                if (shipmentNumber.isNotEmpty()) query += " AND custom_18='$shipmentNumber'"
//                query += " ORDER BY asset_id DESC"
//                it.rawQuery(query)
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<Asset> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.rawQuery("SELECT * FROM tbl_asset WHERE last_update_date > '$lastSyncUpData'")
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsFlagI(): List<Asset> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.rawQuery("SELECT * FROM tbl_asset WHERE update_Flag = 'I' AND create_date <= '${AppSettings.lastSyncUpData} '")
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemByTagOrBarcodeAsync(companyId: Int, locationId: Int, barcodeTag: String): Asset? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<Asset>()
//                    .firstOrNull { asset ->
//                        asset.companyId == companyId &&
//                                asset.locationId == locationId &&
//                                (asset.tag == barcodeTag || asset.barcode == barcodeTag)
//                    }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    fun syncCheckIfBarcodeExist(barcodeTag: String): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val result = it.queryAll<Asset>()
//                    .firstOrNull { asset -> asset.tag == barcodeTag || asset.barcode == barcodeTag }
//                result != null
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    fun assetAddItem(item: Asset): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use { it.insert(item) }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    fun updateAssetByBarcode(item: Asset) {
//        try {
//            val conn = getConnection()
//            conn.use { it.update(item) }
//        } catch (ex: Exception) {
//            val d = ex.message
//        }
//    }
//
//    fun assetCount(): Int {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<Asset>().size }
//        } catch (ex: Exception) {
//            val d = ex.message
//            0
//        }
//    }
//
//    fun getMaxAssetId(): Int {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<Asset>().maxOfOrNull { asset -> asset.assetId } ?: 0
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            0
//        }
//    }
//
//    suspend fun getAssetsForImageSync(): List<Asset> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.rawQuery("SELECT * FROM tbl_asset WHERE image_sync=1")
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun checkIfBarcodeOrTagExist(barcode: String, tag: String): Asset? {
//        return try {
//            val safeBarcode = barcode.ifBlank { null }
//            val safeTag = tag.ifBlank { null }
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<Asset>()
//                    .firstOrNull { asset ->
//                        (safeTag != null && asset.tag == safeTag) ||
//                                (safeBarcode != null && asset.barcode == safeBarcode)
//                    }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getAssetByBarcode(barcode: String): Asset? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<Asset>().firstOrNull { asset -> asset.barcode == barcode }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    /**
//     * Get the Asset if it is from some location from the user
//     * (The asset will be New because it is from a different Job Location)
//     */
//    suspend fun getItemByTagOrBarcodeUserLocationAsync(companyId: Int, userId: Int, asset: String): Asset? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val locationsByUser = it.queryAll<CompanyLocation>()
//                    .filter { loc -> loc.companyId == companyId && loc.responsibleUserId == userId }
//                    .map { loc -> loc.locationId }
//
//                it.queryAll<Asset>().firstOrNull { a ->
//                    a.companyId == companyId &&
//                            locationsByUser.contains(a.locationId) &&
//                            (a.tag == asset || a.barcode == asset)
//                }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemByTagOrBarcodeAsync(
//        companyId: Int,
//        tagAsset: String,
//        barcodeAsset: String,
//        assetId: Int?,
//        deviceId: Int?
//    ): List<Asset> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                var lstObj = mutableListOf<Asset>()
//                val result = it.queryAll<Asset>().filter { a -> a.companyId == companyId }
//
//                if (tagAsset.isNotBlank()) {
//                    lstObj = result.filter { a -> a.tag == tagAsset }.toMutableList()
//                    if (lstObj.isNotEmpty() && assetId != null && deviceId != null) {
//                        val curRecord = lstObj.filter { a -> a.assetId == assetId && a.deviceId == deviceId }
//                        val exists = lstObj.filter { a -> curRecord.none { c -> c.assetId == a.assetId && c.deviceId == a.deviceId } }
//                        if (exists.isEmpty()) lstObj = mutableListOf()
//                    }
//                }
//
//                if (barcodeAsset.isNotBlank() && lstObj.isEmpty()) {
//                    lstObj = result.filter { a -> a.barcode == barcodeAsset }.toMutableList()
//                    if (lstObj.isNotEmpty() && assetId != null && deviceId != null) {
//                        val curRecord = lstObj.filter { a -> a.assetId == assetId && a.deviceId == deviceId }
//                        val exists = lstObj.filter { a -> curRecord.none { c -> c.assetId == a.assetId && c.deviceId == a.deviceId } }
//                        if (exists.isEmpty()) lstObj = mutableListOf()
//                    }
//                }
//
//                lstObj
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsAsync(companyId: Int, locationId: Int?): List<Asset> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                var result = it.queryAll<Asset>().filter { a -> a.companyId == companyId }
//                if (locationId != null) {
//                    result = result.filter { a -> a.locationId == locationId }
//                }
//                result
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsAsync(pCompanyId: Int, pLocationId: Int?, pTitle: String, pBarcode: String): List<Asset> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                var query = "SELECT * FROM tbl_asset WHERE company_id = $pCompanyId"
//
//                if (pLocationId != null) {
//                    query += """ AND location_id IN (WITH RECURSIVE LocationParentName AS
//                        (SELECT Location_id FROM tbl_company_location
//                         WHERE ((Parent_Location_id = 0 AND Location_id = $pLocationId) OR Location_id = $pLocationId)
//                         AND company_id = $pCompanyId
//                         UNION
//                         SELECT l.Location_id FROM LocationParentName as t
//                         INNER JOIN tbl_company_location l ON t.Location_id = l.Parent_Location_id
//                         WHERE company_id = $pCompanyId)
//                        SELECT Location_id FROM LocationParentName)"""
//                }
//
//                if (pTitle.isNotBlank()) query += " AND upper(title) = '${pTitle.uppercase()}'"
//                if (pBarcode.isNotBlank()) query += " AND (upper(barcode) = '${pBarcode.uppercase()}' OR upper(barcode) = '${pBarcode.uppercase()}')"
//
//                it.rawQuery(query)
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun initializeAsync() {
//        throw NotImplementedError("initializeAsync is not implemented")
//    }
//
//    suspend fun pullLatestAsync(): Boolean {
//        throw NotImplementedError("pullLatestAsync is not implemented")
//    }
//
//    suspend fun syncAsync(): Boolean {
//        throw NotImplementedError("syncAsync is not implemented")
//    }
//
//    suspend fun getShipmentItemsAsync(): List<Shipment> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.rawQuery("SELECT * FROM tbl_shipment WHERE update_flag != 'D'")
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun checkIfShipmentNumberExist(shipmentNum: String): Shipment? {
//        return try {
//            val safeNum = shipmentNum.ifBlank { null }
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<Shipment>()
//                    .firstOrNull { s -> safeNum != null && s.shipmentNumber == safeNum }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun checkIfShipmentNumberExistInAsset(shipmentNumber: String): Asset? {
//        return try {
//            val safeNum = shipmentNumber.ifBlank { null }
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<Asset>()
//                    .firstOrNull { a -> safeNum != null && a.custom18 == safeNum }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    // 02/04/2025 Remove All Deactivated Shipment's Assets from Asset Table
//    fun removeDeactivatedShipmentAssets() {
//        try {
//            val conn = getConnection()
//            conn.use {
//                val query = "DELETE FROM tbl_asset WHERE custom_18 IN (SELECT shipment_number FROM tbl_shipment WHERE update_flag = 'D')"
//                val rowsAffected = it.execute(query)
//                Log.d("AssetDataStore", "Deleted $rowsAffected assets from local DB linked to deactivated shipments.")
//            }
//        } catch (ex: Exception) {
//            Log.e("AssetDataStore", "Error removing assets: ${ex.message}")
//        }
//    }
//
//    // 04/08/2025 Remove All Assets from Asset Table within 3-month range
//    suspend fun deleteAssetsWithinLast3MonthsAsync(selectedDate: LocalDate): Int {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val threeMonthsAgo = selectedDate.minusMonths(3)
//                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//                val sql = """
//                    DELETE FROM tbl_asset
//                    WHERE DATE(purchase_date) BETWEEN DATE(?) AND DATE(?)
//                """.trimIndent()
//                it.execute(sql, threeMonthsAgo.format(formatter), selectedDate.format(formatter))
//            }
//        } catch (ex: Exception) {
//            throw Exception("Error while deleting assets within 3 months: ${ex.message}", ex)
//        }
//    }
//}