package com.example.stramitapp.restclient//package com.example.stramitapp.restclient
//
//import android.util.Log
//import com.example.stramitapp.model.New.SimpleDeviceToServerRequest
//import com.example.stramitapp.models.Asset
//import com.example.stramitapp.services.API.Sync.request.DeviceToServerRequest
//import com.example.stramitapp.services.API.Sync.response.DeviceToServerResponse
//import com.example.stramitapp.services.API.Sync.response.GetAssignCompanyListToUserResponse
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import okhttp3.*
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.RequestBody.Companion.asRequestBody
//import okhttp3.RequestBody.Companion.toRequestBody
//import com.google.gson.Gson
//import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
//import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
//import java.io.*
//import com.example.stramitapp.models.Constants.ApiClient
//import com.example.stramitapp.services.API.Sync.request.DownloadCompanyAssignToUserWithDBGzipRequest
//import com.example.stramitapp.services.API.Sync.request.GetAssignCompanyListToUserRequest
//import com.example.stramitapp.services.API.Sync.response.DownloadCompanyAssignToUserWithDBGzipResponse
//
//class SyncClientService : ApiClient() {
//
//    private val gson = Gson()
//    private val httpClient = OkHttpClient()
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Upload Image
//    // ─────────────────────────────────────────────────────────────────────────
//
//    suspend fun uploadImage(
//        item: Asset,
//        request: SimpleDeviceToServerRequest
//    ): DeviceToServerResponse = withContext(Dispatchers.IO) {
//        val result = DeviceToServerResponse()
//        controller = "uploadAssetImage.do"
//
//        try {
//            val resource = "$baseUrl/${request.userId}/${request.currentDeviceUdid}/$controller"
//            val file = File("${AppSettings.pathAssetNewImages}${item.barcode}.jpg")
//
//            val requestBody = MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart(
//                    "fileData",
//                    file.name,
//                    file.asRequestBody("image/jpeg".toMediaType())
//                )
//                .addFormDataPart("assetId", item.assetId.toString())
//                .addFormDataPart("deviceId", item.deviceId.toString())
//                .addFormDataPart("barcode", item.barcode)
//                .addFormDataPart("companyId", item.companyId.toString())
//                .build()
//
//            val httpRequest = Request.Builder()
//                .url(resource)
//                .post(requestBody)
//                .build()
//
//            val response = httpClient.newCall(httpRequest).execute()
//            val body = response.body?.string()
//            if (body != null) {
//                gson.fromJson(body, DeviceToServerResponse::class.java)
//            } else {
//                result
//            }
//        } catch (ex: Exception) {
//            Log.e("SyncClientService", "uploadImage Exception: ${ex.message}", ex)
//            result.apply {
//                statusCode = 0
//                error = ex.message
//            }
//        }
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Device To Server (with syncVersion)
//    // ─────────────────────────────────────────────────────────────────────────
//
//    suspend fun deviceToServer(
//        request: DeviceToServerRequest
//    ): DeviceToServerResponse = withContext(Dispatchers.IO) {
//        val result = DeviceToServerResponse()
//        controller = "deviceToServer.do"
//
//        try {
//            val resource = "$baseUrl/${request.userId}/${request.currentDeviceUdid}/$controller" +
//                    "?syncVersion=${request.syncVersion}"
//
//            val parameters = gson.toJson(request.parameters)
//            val paramSerialize = APIHelper.regexReplace(parameters, "JsonDateTime", " ", "T")
//                .takeIf { it.isNotEmpty() } ?: parameters
//
//            val response = RestClientService.executePostRequestAsync(resource, paramSerialize)
//            if (response != null) {
//                gson.fromJson(response, DeviceToServerResponse::class.java)
//            } else {
//                result
//            }
//        } catch (ex: Exception) {
//            Log.e("SyncClientService", "deviceToServer Exception: ${ex.message}", ex)
//            result.apply {
//                statusCode = 0
//                error = ex.message
//            }
//        }
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Device To Server (simple, no syncVersion)
//    // ─────────────────────────────────────────────────────────────────────────
//
//    suspend fun deviceToServer(
//        request: SimpleDeviceToServerRequest
//    ): DeviceToServerResponse = withContext(Dispatchers.IO) {
//        val result = DeviceToServerResponse()
//        controller = "deviceToServer.do"
//
//        try {
//            val resource = "$baseUrl/${request.userId}/${request.currentDeviceUdid}/$controller"
//
//            val parameters = gson.toJson(request.parameters)
//            // NOTE: args intentionally reversed vs overload above, matching original C#
//            val paramSerialize = APIHelper.regexReplace(parameters, "JsonDateTime", "T", " ")
//                .takeIf { it.isNotEmpty() } ?: parameters
//
//            val response = RestClientService.executePostRequestAsync(resource, paramSerialize)
//            if (response != null) {
//                gson.fromJson(response, DeviceToServerResponse::class.java)
//            } else {
//                result
//            }
//        } catch (ex: Exception) {
//            Log.e("SyncClientService", "deviceToServer (simple) Exception: ${ex.message}", ex)
//            result.apply {
//                statusCode = 0
//                error = ex.message
//            }
//        }
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Download Company DB (GZip / ZIP)
//    // ─────────────────────────────────────────────────────────────────────────
//
//    suspend fun downloadCompanyAssignToUserWithDBGzip(
//        request: DownloadCompanyAssignToUserWithDBGzipRequest
//    ): DownloadCompanyAssignToUserWithDBGzipResponse = withContext(Dispatchers.IO) {
//
//        val result = DownloadCompanyAssignToUserWithDBGzipResponse()
//        controller = "downloadCompanyAssignToUserWithDBGzip.do"
//
//        val dbPath = AppSettings.pathDatabase
//        val pathTempDBZip = File(dbPath, "TempDB.zip")
//        val tempDbExtracted = File(dbPath, "Temp_${AppSettings.databaseName}")
//        val finalDbPath = File(dbPath, AppSettings.databaseName)
//
//        try {
//            // Ensure directory exists
//            File(dbPath).mkdirs()
//
//            // Build request URL
//            val assetCount = App.repository.assetDataStore.assetCount()
//            val resource = buildString {
//                append("$baseUrl/${request.userId}/${request.currentDeviceUdid}/$controller?syncVersion=1.3.0")
//                append("&currentDeviceType=${request.currentDeviceType}")
//                append("&companyId=${request.companyId}")
//                append("&userLastUpdateTimeStamp=${if (assetCount == 0) "0" else request.userLastUpdateTimeStamp}")
//                append("&userId=${request.userId}")
//                append("&currentDeviceUdid=${request.currentDeviceUdid}")
//            }
//
//            // Delete old temp zip if exists
//            if (pathTempDBZip.exists()) pathTempDBZip.delete()
//
//            // Download the zip file
//            val emptyBody = "".toRequestBody("application/json".toMediaType())
//            val httpRequest = Request.Builder().url(resource).post(emptyBody).build()
//
//            httpClient.newCall(httpRequest).execute().use { response ->
//                response.body?.byteStream()?.use { input ->
//                    FileOutputStream(pathTempDBZip).use { output ->
//                        input.copyTo(output)
//                    }
//                }
//            }
//
//            if (pathTempDBZip.length() <= 0L) throw Exception("Downloaded file is empty")
//
//            // Delete old extracted file if exists
//            if (tempDbExtracted.exists()) tempDbExtracted.delete()
//
//            // Extract — Android = GZip, iOS = ZIP
//            if (isAndroid()) {
//                FileInputStream(pathTempDBZip).use { fis ->
//                    GzipCompressorInputStream(BufferedInputStream(fis)).use { gzip ->
//                        FileOutputStream(tempDbExtracted).use { out ->
//                            gzip.copyTo(out)
//                        }
//                    }
//                }
//            } else {
//                ZipArchiveInputStream(BufferedInputStream(FileInputStream(pathTempDBZip))).use { zis ->
//                    var entry = zis.nextZipEntry
//                    while (entry != null) {
//                        if (!entry.isDirectory) {
//                            FileOutputStream(tempDbExtracted).use { out ->
//                                zis.copyTo(out)
//                            }
//                            break // only first file
//                        }
//                        entry = zis.nextZipEntry
//                    }
//                }
//            }
//
//            // Replace main DB if fresh install or no existing assets
//            if (assetCount == 0 || AppSettings.isFreshInstall.equals("Yes", ignoreCase = true)) {
//                if (finalDbPath.exists()) finalDbPath.delete()
//                tempDbExtracted.copyTo(finalDbPath, overwrite = true)
//            }
//
//            if (pathTempDBZip.exists()) pathTempDBZip.delete()
//
//            result.statusCode = 1
//
//        } catch (ex: Exception) {
//            result.statusCode = 0
//            result.error = ex.message
//            Log.e("SyncClientService", "downloadCompanyAssignToUserWithDBGzip error: ${ex.message}", ex)
//        } finally {
//            try {
//                if (pathTempDBZip.exists()) pathTempDBZip.delete()
//            } catch (e: Exception) {
//                Log.w("SyncClientService", "Cleanup failed: ${e.message}")
//            }
//        }
//
//        result
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Get Assign Company List To User
//    // ─────────────────────────────────────────────────────────────────────────
//
//    suspend fun getAssignCompanyListToUser(
//        request: GetAssignCompanyListToUserRequest
//    ): GetAssignCompanyListToUserResponse = withContext(Dispatchers.IO) {
//
//        val result = GetAssignCompanyListToUserResponse()
//        controller = "getAssignCompanyListToUser.do"
//
//        try {
//            val resource = "$baseUrl/${request.userId}/${request.currentDeviceUdid}/$controller" +
//                    "?userId=${request.userId}" +
//                    "&currentDeviceType=${request.currentDeviceType}" +
//                    "&currentDeviceUdid=${request.currentDeviceUdid}"
//
//            val response = RestClientService.executeSimpleGetRequestAsync(resource)
//            if (response != null) {
//                gson.fromJson(response, GetAssignCompanyListToUserResponse::class.java)
//            } else {
//                result
//            }
//        } catch (ex: Exception) {
//            Log.e("SyncClientService", "getAssignCompanyListToUser Exception: ${ex.message}", ex)
//            result.apply {
//                statusCode = 0
//                error = ex.message
//            }
//        }
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Floor Sweep Data Transfer
//    // ─────────────────────────────────────────────────────────────────────────
//
//    suspend fun floorSweepDataTransfer(
//        request: FloorSweepRequest
//    ): FloorSweepResponse = withContext(Dispatchers.IO) {
//
//        val result = FloorSweepResponse()
//        controller = "floorSweep.do"
//
//        try {
//            val tempBaseUrl = baseUrl.replace("/ws", "")
//            val resource = "$tempBaseUrl/sws/$controller"
//
//            val parameters = gson.toJson(request)
//            val paramSerialize = APIHelper.regexReplace(parameters, "JsonDateTime", " ", "T")
//                .takeIf { it.isNotEmpty() } ?: parameters
//
//            val response = RestClientService.executePostRequestAsync(resource, paramSerialize)
//            if (response != null) {
//                gson.fromJson(response, FloorSweepResponse::class.java)
//            } else {
//                result.apply {
//                    statusCode = 0
//                    error = "No response received from server"
//                }
//            }
//        } catch (ex: Exception) {
//            Log.e("SyncClientService", "floorSweepDataTransfer Exception: ${ex.message}", ex)
//            result.apply {
//                statusCode = 0
//                error = ex.message
//            }
//        }
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Helper
//    // ─────────────────────────────────────────────────────────────────────────
//
//    private fun isAndroid(): Boolean =
//        System.getProperty("java.vm.name") == "Dalvik"
//}