package com.example.stramitapp.services

import android.util.Log
import com.example.stramitapp.App
import com.example.stramitapp.models.Database.AppDatabase
import com.example.stramitapp.model.*
import com.example.stramitapp.restclient.SyncClientService
import com.example.stramitapp.services.API.Sync.request.DeviceToServerRequest
import com.example.stramitapp.services.API.Sync.request.DownloadCompanyAssignToUserWithDBGzipRequest
import com.example.stramitapp.services.API.Sync.request.GetAssignCompanyListToUserRequest
import com.example.stramitapp.services.API.Sync.response.GetAssignCompanyListToUserResponse
import com.example.stramitapp.utilities.AppSettings
import com.example.stramitapp.utilities.SecurePrefs
import com.example.stramitapp.models.Constants.StorageKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
class SyncService {

    private val syncClientService = SyncClientService()

    companion object {
        private const val FLAG_BATCH_SIZE = 300
    }

    suspend fun sync(): Boolean = withContext(Dispatchers.IO) {
        try {
            AppSettings.isForceSyncRequested = false
            forceSync_internal()
        } catch (ex: Exception) {
            Log.e("SyncService", "sync() error: ${ex.message}", ex)
            false
        }
    }

    suspend fun forceSync(): Boolean = withContext(Dispatchers.IO) {
        try {
            AppSettings.isForceSyncRequested = true
            forceSync_internal()
        } catch (ex: Exception) {
            Log.e("SyncService", "forceSync() error: ${ex.message}", ex)
            AppSettings.isForceSyncRequested = false
            false
        }
    }

    private suspend fun forceSync_internal(): Boolean {

        if (AppSettings.authenticatedUser == null) {
            Log.w("SyncService", "authenticatedUser is null — loading from local DB...")
            val userFromDb = withContext(Dispatchers.IO) {
                AppDatabase.getInstance().userDao().getFirstUser()
            }
            if (userFromDb == null) {
                Log.e("SyncService", "No user in local DB. Cannot sync — user must log in first.")
                return false
            }
            AppSettings.authenticatedUser = userFromDb
            Log.d("SyncService", "Loaded user from DB: userId=${userFromDb.userId}")
        }

        val deviceToServerSuccess = syncDeviceToServer()
        if (!deviceToServerSuccess) {
            Log.e("SyncService", "Device→Server failed. Aborting.")
            return false
        }
        return syncServerToDevice()
    }
    private suspend fun syncDeviceToServer(): Boolean {
        getSettings()

        val user = AppSettings.authenticatedUser ?: run {
            Log.e("SyncService", "authenticatedUser still null in syncDeviceToServer")
            return false
        }

        if (AppSettings.isFreshInstall != "No") {
            Log.d("SyncService", "Fresh install — skipping Device→Server.")
            return true
        }

        return try {
            val currentDb = AppDatabase.getInstance()

            val objAsset: List<Asset> = currentDb.assetDao()
                .getItemsToExport(AppSettings.lastSyncUpData)

            val objAssetMovement: List<AssetMovementInfo> = currentDb
                .assetMovementInfoDao()
                .getItemsToExport(AppSettings.lastSyncUpData)

            Log.d("SyncService", "Uploading ${objAsset.size} assets, ${objAssetMovement.size} movements")

            if (objAsset.isEmpty() && objAssetMovement.isEmpty()) {
                Log.d("SyncService", "Nothing to sync. Skipping deviceToServer call.")
                return true
            }

            val request = DeviceToServerRequest(
                syncVersion       = AppSettings.syncVersion,
                userId            = user.userId,
                currentDeviceUdid = user.currentDeviceUdid ?: AppSettings.deviceUdid,
                parameters        = DeviceToServerRequest.Parameters(
                    tblAssetExtraInfo = DeviceToServerRequest.AssetExtraInfoList(
                        tblAssetMovementInfos    = objAssetMovement,
                        tblAssetMemoInfos        = emptyList(),
                        tblAssetMaintenanceInfos = emptyList(),
                        tblAssetLeaseInfos       = emptyList(),
                        tblAssetInsuranceInfos   = emptyList(),
                        tblAssetInspectionInfos  = emptyList(),
                        tblAssetFinancialInfos   = emptyList(),
                        tblAssetBOMInfos         = emptyList()
                    ),
                    tblAssets = objAsset
                )
            )

            val response = syncClientService.deviceToServer(request)
            Log.d("SyncService", "deviceToServer response: statusCode=${response.statusCode}, error=${response.error}")

            if (response.statusCode == 1) {
                val now = nowDateTimeSQLite()
                SecurePrefs.set(StorageKeys.LastSyncUpDataStorageKey, now)
                AppSettings.lastSyncUpData = now
                updateAssetsFlag()
                deleteMovementHistory()
                Log.d("SyncService", "Device→Server sync succeeded.")
                true
            } else {
                val msg = response.error?.takeIf { it.isNotEmpty() } ?: "Syncing has failed."
                Log.e("SyncService", "Device→Server sync failed: $msg")
                false
            }

        } catch (ex: Exception) {
            Log.e("SyncService", "syncDeviceToServer exception: ${ex.message}", ex)
            false
        }
    }
    //30-09-2025 code Updated
    private suspend fun syncServerToDevice(): Boolean {
        getSettings()

        val user = AppSettings.authenticatedUser ?: run {
            Log.e("SyncService", "authenticatedUser null in syncServerToDevice")
            return false
        }

        val forceFullSync = AppSettings.isForceSyncRequested

        return try {
            val companiesResult = getAssignCompanyListToUser(user)
            if (companiesResult.statusCode != 1) {
                Log.e("SyncService", "GetAssignCompanyList failed: ${companiesResult.error}")
                AppSettings.isForceSyncRequested = false
                return false
            }

            val companies = companiesResult.list ?: emptyList()
            Log.d("SyncService", "Found ${companies.size} companies to sync")
            var synced = false

            for (company in companies) {
                val performFullSync: Boolean = when {
                    forceFullSync -> {
                        Log.d("SyncService", "Force sync from UI. Full sync.")
                        true
                    }
                    AppSettings.lastSyncData == null -> {
                        Log.d("SyncService", "No previous sync. Full sync.")
                        true
                    }
                    else -> {
                        val daysSince = TimeUnit.MILLISECONDS.toDays(
                            System.currentTimeMillis() - AppSettings.lastSyncData!!
                        )
                        Log.d("SyncService", "Days since last sync: $daysSince")
                        daysSince > 9
                    }
                }

                val dateTimeStamp = if (performFullSync) "0"
                else AppSettings.lastSyncData!!.toString()

                val downloadRequest = DownloadCompanyAssignToUserWithDBGzipRequest(
                    userId                  = user.userId,
                    currentDeviceType       = AppSettings.deviceType,
                    currentDeviceUdid       = user.currentDeviceUdid ?: AppSettings.deviceUdid,
                    companyId               = company.companyId,
                    userLastUpdateTimeStamp = dateTimeStamp
                )

                val downloadResult = syncClientService.downloadCompanyAssignToUserWithDBGzip(downloadRequest)
                Log.d("SyncService", "Download result: statusCode=${downloadResult.statusCode}, error=${downloadResult.error}")

                if (downloadResult.statusCode == 1) {
                    synced = if (performFullSync) {
                        performFullDatabaseSync(company.companyId)
                    } else if (AppSettings.isFreshInstall == "No") {
                        insertUpdateAllTables(companyId = company.companyId)
                    } else true

                    SecurePrefs.set(StorageKeys.IsFreshInstall, "No")
                    AppSettings.isFreshInstall = "No"
                } else {
                    Log.e("SyncService", "Download failed: ${downloadResult.error}")
                    AppSettings.isForceSyncRequested = false
                    return false
                }
            }

            if (synced) {
                val now = nowDateTimeSQLite()
                SecurePrefs.set(StorageKeys.LastSyncDataStorageKey, now)
                AppSettings.lastSyncData = System.currentTimeMillis()
                AppSettings.isForceSyncRequested = false
                Log.d("SyncService", "Server→Device sync succeeded.")
            }

            synced

        } catch (ex: Exception) {
            Log.e("SyncService", "syncServerToDevice exception: ${ex.message}", ex)
            AppSettings.isForceSyncRequested = false
            false
        }
    }

    private suspend fun performFullDatabaseSync(companyId: Int): Boolean {
        return try {
            val mainDb = File(AppSettings.pathDatabase, AppSettings.databaseName)
            val tempDb = File(AppSettings.pathDatabase, "Temp_${AppSettings.databaseName}")

            if (!tempDb.exists()) {
                Log.e("SyncService", "Temp DB not found for full sync.")
                return false
            }

            // Step 1 — Close SQLite connection only (does NOT cancel coroutine scope)
            AppDatabase.resetInstance()
            Log.d("SyncService", "Room instance closed and reset.")

            // Step 2 — Delete old DB files so Room starts completely fresh
            if (mainDb.exists()) mainDb.delete()
            File("${mainDb.path}-wal").takeIf { it.exists() }?.delete()
            File("${mainDb.path}-shm").takeIf { it.exists() }?.delete()
            Log.d("SyncService", "Old DB files deleted.")

            // Step 3 — Re-init Room with a fresh schema
            AppDatabase.init(AppSettings.appContext)
            Log.d("SyncService", "Room re-initialized with fresh schema.")

            (AppSettings.appContext as? App)?.reinitializeRepository()
            Log.d("SyncService", "Repository re-initialized with fresh DAOs.")

            // Step 5 — Merge temp DB data into the new Room DB
            insertUpdateAllTables(companyId = companyId)

        } catch (ex: Exception) {
            Log.e("SyncService", "performFullDatabaseSync error: ${ex.message}", ex)
            false
        }
    }

    private suspend fun insertUpdateAllTables(companyId: Int): Boolean =
        withContext(Dispatchers.IO) {
            val tempDbPath = File(
                AppSettings.pathDatabase,
                "Temp_${AppSettings.databaseName}"
            ).absolutePath

            val rawDb = AppDatabase.getInstance().openHelper.writableDatabase
            var success = true

            try {
                rawDb.execSQL("ATTACH DATABASE '$tempDbPath' AS secondDB")

                // tbl_user first (FK dependency)
                val userCols = getColumnNames(rawDb, "tbl_user")
                if (userCols.isNotEmpty()) {
                    rawDb.execSQL(
                        "INSERT OR REPLACE INTO tbl_user " +
                                "SELECT ${userCols.joinToString(",")} FROM secondDB.tbl_user"
                    )
                    Log.d("SyncService", "tbl_user merged (${userCols.size} cols)")
                }

                val mainTables   = getTableNames(rawDb, null)
                val secondTables = getTableNames(rawDb, "secondDB")

                for (table in mainTables) {
                    if (table == "tbl_user" || table == "tbl_asset_movement_info") continue
                    if (!secondTables.contains(table)) continue

                    val cols = getColumnNames(rawDb, table)
                    if (cols.isEmpty()) continue

                    rawDb.execSQL(
                        "INSERT OR REPLACE INTO $table " +
                                "SELECT ${cols.joinToString(",")} FROM secondDB.$table"
                    )
                    Log.d("SyncService", "Merged $table (${cols.size} cols)")
                }

            } catch (ex: Exception) {
                Log.e("SyncService", "insertUpdateAllTables error: ${ex.message}", ex)
                success = false
            } finally {
                try { rawDb.execSQL("DETACH DATABASE secondDB") } catch (_: Exception) {}
            }

            if (success) {
                updateAssetsFlag()
                deleteMovementHistory()
            }
            success
        }

    private fun getSettings() {
        AppSettings.lastSyncUpData =
            SecurePrefs.get(StorageKeys.LastSyncUpDataStorageKey)
                ?: "2000-01-01 00:00:00.000"

        AppSettings.lastSyncUpJob =
            SecurePrefs.get(StorageKeys.LastSyncUpJobStorageKey)
                ?: "2000-01-01 00:00:00.000"

        val lastSyncStr = SecurePrefs.get(StorageKeys.LastSyncDataStorageKey)
        AppSettings.lastSyncData =
            if (AppSettings.isFreshInstall == "No" && lastSyncStr != null) {
                lastSyncStr.toLongOrNull()
                    ?: runCatching {
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
                            .parse(lastSyncStr)?.time
                    }.getOrNull()
            } else null
    }

    private suspend fun getAssignCompanyListToUser(user: User): GetAssignCompanyListToUserResponse {
        return try {
            syncClientService.getAssignCompanyListToUser(
                GetAssignCompanyListToUserRequest(
                    userId            = user.userId,
                    currentDeviceType = AppSettings.deviceType,
                    currentDeviceUdid = user.currentDeviceUdid ?: AppSettings.deviceUdid
                )
            )
        } catch (ex: Exception) {
            GetAssignCompanyListToUserResponse().apply {
                statusCode = 0; error = ex.message
            }
        }
    }

    private suspend fun updateAssetsFlag() = withContext(Dispatchers.IO) {
        try {
            val dao   = AppDatabase.getInstance().assetDao()
            val now   = nowDateTimeSQLite()
            val total = dao.getItemsFlagICount()
            var offset = 0

            while (offset < total) {
                val batch = dao.getItemsFlagIPaged(limit = FLAG_BATCH_SIZE, offset = offset)
                if (batch.isEmpty()) break

                batch.forEach { asset ->
                    asset.updateFlag     = "U"
                    asset.lastUpdateDate = now
                    dao.update(asset)
                }
                offset += FLAG_BATCH_SIZE
            }

            Log.d("SyncService", "updateAssetsFlag done — updated $total assets.")
        } catch (ex: Exception) {
            Log.e("SyncService", "updateAssetsFlag error: ${ex.message}", ex)
        }
    }

    private suspend fun deleteMovementHistory() = withContext(Dispatchers.IO) {
        try {
            AppDatabase.getInstance().assetMovementInfoDao().deleteAll()
        } catch (ex: Exception) {
            Log.e("SyncService", "deleteMovementHistory error: ${ex.message}", ex)
        }
    }

    private fun getTableNames(
        rawDb: androidx.sqlite.db.SupportSQLiteDatabase,
        schema: String?
    ): List<String> {
        val tables = mutableListOf<String>()
        val query = if (schema != null)
            "SELECT name FROM $schema.sqlite_master WHERE type='table' AND name LIKE 'tbl_%' ORDER BY name"
        else
            "SELECT name FROM sqlite_master WHERE type='table' AND name LIKE 'tbl_%' ORDER BY name"
        rawDb.query(query).use { c -> while (c.moveToNext()) tables.add(c.getString(0)) }
        return tables
    }

    private fun getColumnNames(
        rawDb: androidx.sqlite.db.SupportSQLiteDatabase,
        table: String
    ): List<String> {
        val cols = mutableListOf<String>()
        rawDb.query("PRAGMA table_info($table)").use { c ->
            val idx = c.getColumnIndex("name")
            while (c.moveToNext()) if (idx >= 0) cols.add(c.getString(idx))
        }
        return cols
    }

    private fun nowDateTimeSQLite(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date())
    }
}