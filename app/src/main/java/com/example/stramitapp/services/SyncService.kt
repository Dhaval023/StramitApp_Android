package com.example.stramitapp.services

import android.util.Log
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

class SyncService(private val db: AppDatabase) {

    private val syncClientService = SyncClientService()

    // ─────────────────────────────────────────────────────────────────────────
    // Public entry points
    // ─────────────────────────────────────────────────────────────────────────

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

    // ─────────────────────────────────────────────────────────────────────────
    // Core pipeline
    // ─────────────────────────────────────────────────────────────────────────

    private suspend fun forceSync_internal(): Boolean {

        // ── CRITICAL FIX ──────────────────────────────────────────────────────
        // The log shows NullPointerException at SyncService.kt:94 on this line:
        //   userId = AppSettings.authenticatedUser!!.userId
        //
        // Root cause: AppSettings.authenticatedUser is never set after login.
        // The log shows the user IS logged in (userId=27, Mitesh Trivedi) but
        // it was saved somewhere else (likely UserDao or SecurePrefs) and
        // never copied into AppSettings.authenticatedUser.
        //
        // Fix: if null, load the user from the local Room DB before proceeding.
        // ─────────────────────────────────────────────────────────────────────
        if (AppSettings.authenticatedUser == null) {
            Log.w("SyncService", "authenticatedUser is null — loading from local DB...")
            val userFromDb = withContext(Dispatchers.IO) {
                db.userDao().getFirstUser()
            }
            if (userFromDb == null) {
                Log.e("SyncService", "No user in local DB either. Cannot sync — user must log in first.")
                return false
            }
            AppSettings.authenticatedUser = userFromDb
            Log.d("SyncService", "Loaded user from DB: userId=${userFromDb.userId}, udid=${userFromDb.currentDeviceUdid}")
        }

        val deviceToServerSuccess = syncDeviceToServer()
        if (!deviceToServerSuccess) {
            Log.e("SyncService", "Device→Server failed. Aborting.")
            return false
        }
        return syncServerToDevice()
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Step 1 — Device → Server
    // ─────────────────────────────────────────────────────────────────────────

    private suspend fun syncDeviceToServer(): Boolean {
        getSettings()

        // Safe — guaranteed non-null after forceSync_internal() guard above
        val user = AppSettings.authenticatedUser ?: run {
            Log.e("SyncService", "authenticatedUser still null in syncDeviceToServer")
            return false
        }

        return try {
            val objAsset: List<Asset> = if (AppSettings.isFreshInstall == "No") {
                db.assetDao().getItemsToExport(AppSettings.lastSyncUpData)
            } else emptyList()

            val objAssetMovement: List<AssetMovementInfo> = if (AppSettings.isFreshInstall == "No") {
                db.assetMovementInfoDao().getItemsToExport(AppSettings.lastSyncUpData)
            } else emptyList()

            Log.d("SyncService", "Uploading ${objAsset.size} assets, ${objAssetMovement.size} movements")
            if (objAsset.isEmpty() && objAssetMovement.isEmpty()) {
                Log.d("SyncService", "Nothing to sync. Skipping deviceToServer call.")
                return true
            }

            val request = DeviceToServerRequest(
                syncVersion = AppSettings.syncVersion,
                userId = user.userId,
                currentDeviceUdid = user.currentDeviceUdid ?: AppSettings.deviceUdid,
                parameters = DeviceToServerRequest.Parameters(
                    tblAssetExtraInfo = DeviceToServerRequest.AssetExtraInfoList(
                        tblAssetMovementInfos = objAssetMovement,
                        tblAssetMemoInfos = emptyList(),
                        tblAssetMaintenanceInfos = emptyList(),
                        tblAssetLeaseInfos = emptyList(),
                        tblAssetInsuranceInfos = emptyList(),
                        tblAssetInspectionInfos = emptyList(),
                        tblAssetFinancialInfos = emptyList(),
                        tblAssetBOMInfos = emptyList()
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

    // ─────────────────────────────────────────────────────────────────────────
    // Step 2 — Server → Device
    // ─────────────────────────────────────────────────────────────────────────

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
                    userId = user.userId,
                    currentDeviceType = AppSettings.deviceType,
                    currentDeviceUdid = user.currentDeviceUdid ?: AppSettings.deviceUdid,
                    companyId = company.companyId,
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

    // ─────────────────────────────────────────────────────────────────────────
    // Full DB replacement
    // ─────────────────────────────────────────────────────────────────────────

    private suspend fun performFullDatabaseSync(companyId: Int): Boolean {
        return try {
            val mainDb = File(AppSettings.pathDatabase, AppSettings.databaseName)
            val tempDb = File(AppSettings.pathDatabase, "Temp_${AppSettings.databaseName}")

            if (!tempDb.exists()) {
                Log.e("SyncService", "Temp DB not found for full sync.")
                return false
            }

            db.close()
            if (mainDb.exists()) mainDb.delete()
            tempDb.copyTo(mainDb, overwrite = true)
            Log.d("SyncService", "Full DB replaced.")

            insertUpdateAllTables(companyId = companyId)
        } catch (ex: Exception) {
            Log.e("SyncService", "performFullDatabaseSync error: ${ex.message}", ex)
            false
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Incremental table merge
    // ─────────────────────────────────────────────────────────────────────────

    private suspend fun insertUpdateAllTables(companyId: Int): Boolean =
        withContext(Dispatchers.IO) {
            val mainDbPath = File(AppSettings.pathDatabase, AppSettings.databaseName).absolutePath
            val tempDbPath = File(AppSettings.pathDatabase, "Temp_${AppSettings.databaseName}").absolutePath
            val rawDb = db.openHelper.writableDatabase
            var success = true

            try {
                rawDb.execSQL("ATTACH DATABASE '$mainDbPath' AS firstDB")
                rawDb.execSQL("ATTACH DATABASE '$tempDbPath' AS secondDB")

                val userCols = getColumnNames(rawDb, "firstDB", "tbl_user")
                if (userCols.isNotEmpty()) {
                    rawDb.execSQL(
                        "INSERT OR REPLACE INTO firstDB.tbl_user " +
                                "SELECT ${userCols.joinToString(",")} FROM secondDB.tbl_user"
                    )
                }

                val firstTables  = getTableNames(rawDb, "firstDB")
                val secondTables = getTableNames(rawDb, "secondDB")

                for (table in firstTables) {
                    if (table == "tbl_user" || table == "tbl_asset_movement_info") continue
                    if (!secondTables.contains(table)) continue
                    val cols = getColumnNames(rawDb, "firstDB", table)
                    if (cols.isEmpty()) continue
                    rawDb.execSQL(
                        "INSERT OR REPLACE INTO firstDB.$table " +
                                "SELECT ${cols.joinToString(",")} FROM secondDB.$table"
                    )
                }
            } catch (ex: Exception) {
                Log.e("SyncService", "insertUpdateAllTables error: ${ex.message}", ex)
                success = false
            } finally {
                try { rawDb.execSQL("DETACH DATABASE firstDB") } catch (_: Exception) {}
                try { rawDb.execSQL("DETACH DATABASE secondDB") } catch (_: Exception) {}
            }

            if (success) {
                updateAssetsFlag()
                deleteMovementHistory()
            }
            success
        }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

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
                    userId = user.userId,
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
            val now = nowDateTimeSQLite()
            db.assetDao().getItemsFlagI().forEach { asset ->
                asset.updateFlag = "U"
                asset.lastUpdateDate = now
                db.assetDao().update(asset)
            }
        } catch (ex: Exception) {
            Log.e("SyncService", "updateAssetsFlag error: ${ex.message}", ex)
        }
    }

    private suspend fun deleteMovementHistory() = withContext(Dispatchers.IO) {
        try {
            db.assetMovementInfoDao().deleteAll()
        } catch (ex: Exception) {
            Log.e("SyncService", "deleteMovementHistory error: ${ex.message}", ex)
        }
    }

    private fun getTableNames(
        rawDb: androidx.sqlite.db.SupportSQLiteDatabase,
        schema: String
    ): List<String> {
        val tables = mutableListOf<String>()
        rawDb.query(
            "SELECT name FROM $schema.sqlite_master WHERE type='table' AND name LIKE 'tbl_%' ORDER BY name"
        ).use { c -> while (c.moveToNext()) tables.add(c.getString(0)) }
        return tables
    }

    private fun getColumnNames(
        rawDb: androidx.sqlite.db.SupportSQLiteDatabase,
        schema: String,
        table: String
    ): List<String> {
        val cols = mutableListOf<String>()
        rawDb.query("PRAGMA $schema.table_info($table)").use { c ->
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