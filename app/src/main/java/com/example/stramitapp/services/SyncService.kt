package com.example.stramitapp.services

import android.util.Log
import com.example.stramitapp.models.Database.AppDatabase
import com.example.stramitapp.model.*
import com.example.stramitapp.restclient.SyncClientService
import com.example.stramitapp.services.API.Sync.request.DeviceToServerRequest
import com.example.stramitapp.services.API.Sync.response.DeviceToServerResponse
import kotlinx.coroutines.Dispatchers
import com.example.stramitapp.utilities.AppSettings
import kotlinx.coroutines.withContext

class SyncService(private val db: AppDatabase) {

    private val syncClientService = SyncClientService()

    suspend fun syncToServer(
        userId: Int,
        deviceId: String
    ): DeviceToServerResponse = withContext(Dispatchers.IO) {

        try {

            val assets = db.assetDao().getAll()

            val movementInfos = db.assetMovementInfoDao().getAll()
            val memoInfos = db.assetMemoInfoDao().getAll()
            val maintenanceInfos = db.assetMaintenanceInfoDao().getAll()
            val leaseInfos = db.assetLeaseInfoDao().getAll()
            val insuranceInfos = db.assetInsuranceInfoDao().getAll()
            val inspectionInfos = db.assetInspectionInfoDao().getAll()
            val financialInfos = db.assetFinancialInfoDao().getAll()
//            val bomInfos = db.billOfMaterialDao().getAll()

            // ─────────────────────────────────────
            // Build Request
            // ─────────────────────────────────────

            val extraInfo = DeviceToServerRequest.AssetExtraInfoList(
                tblAssetMovementInfos = movementInfos,
                tblAssetMemoInfos = memoInfos,
                tblAssetMaintenanceInfos = maintenanceInfos,
                tblAssetLeaseInfos = leaseInfos,
                tblAssetInsuranceInfos = insuranceInfos,
                tblAssetInspectionInfos = inspectionInfos,
                tblAssetFinancialInfos = financialInfos,
//                tblAssetBOMInfos = bomInfos
            )

            val parameters = DeviceToServerRequest.Parameters(
                tblAssetExtraInfo = extraInfo,
                tblAssets = assets
            )

            val request = DeviceToServerRequest(
                syncVersion = "1.3.0",
                userId = userId,
                currentDeviceUdid = deviceId,
                parameters = parameters
            )

            // ─────────────────────────────────────
            // Call API
            // ─────────────────────────────────────

            val response = syncClientService.deviceToServer(request)

            if (response.statusCode == 1) {
                Log.d("SYNC", "Sync completed successfully")
            } else {
                Log.e("SYNC", "Sync failed: ${response.error}")
            }

            response

        } catch (ex: Exception) {

            Log.e("SYNC", "Sync error: ${ex.message}", ex)

            DeviceToServerResponse().apply {
                statusCode = 0
                error = ex.message
            }
        }
    }

    suspend fun sync(): Boolean = withContext(Dispatchers.IO) {
        try {
            // 1. Upload local changes to server
            val uploadResult = APIHelper.uploadLocalChanges()
            if (!uploadResult) return@withContext false

            val downloadResult = APIHelper.downloadServerData()
            if (!downloadResult) return@withContext false

            AppSettings.lastSyncData = System.currentTimeMillis()

            true
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }
    suspend fun forceSync(): Boolean = withContext(Dispatchers.IO) {
        try {
            // 1. Delete local DB completely
            val dbFile = AppSettings.pathDatabase + "/" + AppSettings.databaseName
            val db = AppDatabase.getInstance()
            db.clearAllTables() // Clear all tables in Room
            // Optional: delete file from storage
            // File(dbFile).delete()

            // 2. Download full DB from server
            val downloadResult = APIHelper.downloadFullDatabase()
            if (!downloadResult) return@withContext false

            // 3. Reset last sync info
            AppSettings.lastSyncData = System.currentTimeMillis()

            true
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }
}