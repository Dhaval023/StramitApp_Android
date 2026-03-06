package com.example.stramitapp.services

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.stramitapp.utilities.AppSettings
import com.example.stramitapp.models.Constants.StorageKeys
import com.example.stramitapp.services.API.Sync.request.DeviceToServerRequest
import com.example.stramitapp.restclient.SyncClientService
import java.text.SimpleDateFormat
import java.util.*

class APIHelper(private val context: Context) {

    suspend fun deviceServer(): Boolean {
        var result = false

        try {
            getSettings()

            val objAsset =
                App.repository.assetDataStore.getItemsToExport(AppSettings.lastSyncUpData)

            val request = DeviceToServerRequest(
                syncVersion = "1.3.0",
                userId = AppSettings.authenticatedUser!!.userId,
                currentDeviceUdid = AppSettings.authenticatedUser?.currentDeviceUdid,
                parameters = DeviceToServerRequest.Parameters(
                    tblAssets = objAsset
                )
            )

            val response = SyncClientService().deviceToServer(request)

            if (response.statusCode == 1) {

                SecureStorageHelper.set(
                    context,
                    StorageKeys.LastSyncUpDataStorageKey,
                    nowDateTimeSQLite()
                )

                result = true
            }

        } catch (e: Exception) {
            result = false
        }

        return result
    }
    private fun getSettings() {
        AppSettings.lastSyncUpData =
            SecureStorageHelper.get(context, StorageKeys.LastSyncUpDataStorageKey).toString()

        AppSettings.authenticatedUser =
            AppSettings.authenticatedUser  // or load from storage if needed
    }
    private fun nowDateTimeSQLite(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date())
    }
    companion object {

        fun regexReplace(
            originalText: String,
            searchExpression: String,
            replaceValue: String,
            newValue: String
        ): String {

            var regexExpression = ""

            if (searchExpression == "JsonDateTime") {
                regexExpression =
                    """(?:[2-9]\d\d\d)-(?:1[012]|0?[1-9])-(?:2[0-8]|1\d|0?[1-9])"""
            }

            return if (regexExpression.isNotEmpty()) {
                Regex(regexExpression).replace(originalText) {
                    it.value.replace(replaceValue, newValue)
                }
            } else originalText
        }
        fun nowDateTimeSQLite(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            return sdf.format(Date())
        }

        suspend fun uploadLocalChanges(): Boolean = withContext(Dispatchers.IO) {
            try {
                val assetsToUpload = App.repository.assetDataStore.getItemsToExport(AppSettings.lastSyncUpData)
                val memosToUpload = App.repository.assetMemoInfoDataStore.getItemsToExportAsync(AppSettings.lastSyncUpData)
                val maintenanceToUpload = App.repository.assetMaintenanceInfoDataStore.getItemsToExportAsync(AppSettings.lastSyncUpData)

                // Use your existing SyncClientService deviceToServer calls
                val assetResult = SyncClientService().deviceToServer(
                    request = com.example.stramitapp.services.API.Sync.request.DeviceToServerRequest(
                        syncVersion = "1.3.0",
                        userId = AppSettings.authenticatedUser!!.userId,
                        currentDeviceUdid = AppSettings.authenticatedUser?.currentDeviceUdid,
                        parameters = com.example.stramitapp.services.API.Sync.request.DeviceToServerRequest.Parameters(tblAssets = assetsToUpload)
                    )
                )

                // TODO: Upload memos, maintenance, etc. similarly using SyncClientService

                assetResult.statusCode == 1
            } catch (ex: Exception) {
                ex.printStackTrace()
                false
            }
        }

        suspend fun downloadServerData(): Boolean = withContext(Dispatchers.IO) {
            try {
                val response = SyncClientService().downloadCompanyAssignToUserWithDBGzip(
                    request = com.example.stramitapp.services.API.Sync.request.DownloadCompanyAssignToUserWithDBGzipRequest(
                        userId = AppSettings.authenticatedUser!!.userId,
                        currentDeviceUdid = AppSettings.authenticatedUser!!.currentDeviceUdid,
                        currentDeviceType = AppSettings.deviceType,
                        companyId = AppSettings.selectedCompany?.companyWpId ?: 0,
                        userLastUpdateTimeStamp = AppSettings.lastSyncUpData
                    )
                )

                response.statusCode == 1
            } catch (ex: Exception) {
                ex.printStackTrace()
                false
            }
        }

        suspend fun downloadFullDatabase(): Boolean = withContext(Dispatchers.IO) {
            try {
                val response = SyncClientService().downloadCompanyAssignToUserWithDBGzip(
                    request = com.example.stramitapp.services.API.Sync.request.DownloadCompanyAssignToUserWithDBGzipRequest(
                        userId = AppSettings.authenticatedUser!!.userId,
                        currentDeviceUdid = AppSettings.authenticatedUser!!.currentDeviceUdid,
                        currentDeviceType = AppSettings.deviceType,
                        companyId = AppSettings.selectedCompany?.companyWpId ?: 0,
                        userLastUpdateTimeStamp = "0" // force download all
                    )
                )

                response.statusCode == 1
            } catch (ex: Exception) {
                ex.printStackTrace()
                false
            }
        }
    }
}