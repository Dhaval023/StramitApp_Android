package com.example.stramitapp.Repositories

import android.util.Log
import com.example.stramitapp.Dao.AssetDao
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
import com.example.stramitapp.model.Asset
import com.example.stramitapp.model.Shipment
import java.text.SimpleDateFormat
import java.util.*

class AssetDataStore :
    BaseRepository<Asset>(),
    IDataStore<Asset> {

    private val dao: AssetDao by lazy { db.assetDao() }

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override suspend fun getItemAsync(id: Int): Asset? {
        return dao.getById(id)
    }

    suspend fun getNewIdAsync(): Int {
        return (dao.getMaxAssetId() ?: 0) + 1
    }

    override suspend fun addItemAsync(item: Asset): Boolean {
        return try {
            item.assetId = getNewIdAsync()
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("AssetDataStore", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: Asset): Boolean {
        return try {
            dao.update(item) > 0
        } catch (e: Exception) {
            Log.e("AssetDataStore", "Update failed", e)
            false
        }
    }

    override suspend fun deleteItemAsync(item: Asset): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (e: Exception) {
            Log.e("AssetDataStore", "Delete failed", e)
            false
        }
    }

    override suspend fun clearAsync(): Boolean {
        return try {
            dao.clearAll()
            true
        } catch (e: Exception) {
            Log.e("AssetDataStore", "Clear failed", e)
            false
        }
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<Asset> {
        return dao.getActiveAssets()
    }

    suspend fun assetCount(): Int = dao.count()

    suspend fun getMaxAssetId(): Int = dao.getMaxAssetId() ?: 0

    // ---------------- EXPORT ----------------

    suspend fun getItemsToExport(lastSync: String): List<Asset> =
        dao.getItemsToExport(lastSync)

    suspend fun getItemsFlagI(): List<Asset> =
        dao.getItemsFlagI()

    suspend fun getAssetsForImageSync(): List<Asset> =
        dao.getAssetsForImageSync()

    // ---------------- SHIPMENT ----------------

    suspend fun getShipmentItems(): List<Shipment> =
        dao.getShipments()

    suspend fun checkIfShipmentNumberExist(number: String): Shipment? =
        dao.getShipment(number)

    suspend fun removeDeactivatedShipmentAssets(): Int =
        dao.removeDeactivatedShipmentAssets()

    suspend fun deleteAssetsWithinLast3Months(selectedDate: Date): Int {

        val toDate = formatter.format(selectedDate)

        val fromDate = formatter.format(
            Calendar.getInstance().apply {
                time = selectedDate
                add(Calendar.MONTH, -3)
            }.time
        )

        return dao.deleteAssetsBetween(fromDate, toDate)
    }

    override suspend fun initializeAsync() {}

    override suspend fun pullLatestAsync(): Boolean = false

    override suspend fun syncAsync(): Boolean = false
}