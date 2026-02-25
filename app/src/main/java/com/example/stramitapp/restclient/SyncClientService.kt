package com.example.stramitapp.restclient//package com.example.stramitapp.restclient
//
//import android.util.Log
//import com.example.stramitapp.models.Constants.ApiClient
//import com.example.stramitapp.models.request.sync.*
//import com.example.stramitapp.models.response.sync.*
//import com.google.gson.Gson
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.MultipartBody
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.RequestBody.Companion.asRequestBody
//import okhttp3.RequestBody.Companion.toRequestBody
//import java.io.File
//import java.io.FileInputStream
//import java.io.FileOutputStream
//import java.util.zip.GZIPInputStream
//import java.util.concurrent.TimeUnit
//
//class SyncClientService : ApiClient() {
//
//    // ─── Upload Asset Image ───────────────────────────────────────────
//    // C#: UploadImage()
//    suspend fun uploadImage(
//        barcode: String,
//        assetId: Int,
//        deviceId: Int,
//        companyId: Int,
//        userId: Int,
//        currentDeviceUdid: String,
//        pathAssetNewImages: String
//    ): DeviceToServerResponse {
//
//        val controller = "uploadAssetImage.do"
//
//        return try {
//            val resource = "$baseUrl/$userId/$currentDeviceUdid/$controller"
//            val file = File("$pathAssetNewImages$barcode.jpg")
//
//            if (!file.exists()) {
//                return DeviceToServerResponse().apply {
//                    statusCode = 0
//                    error = "Image file not found: ${file.path}"
//                }
//            }
//
//            Log.d("SyncClientService", "Upload URL: $resource")
//
//            val requestBody = MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart(
//                    "fileData",
//                    file.name,
//                    file.asRequestBody("image/jpeg".toMediaType())
//                )
//                .addFormDataPart("assetId", assetId.toString())
//                .addFormDataPart("deviceId", deviceId.toString())
//                .addFormDataPart("barcode", barcode)
//                .addFormDataPart("companyId", companyId.toString())
//                .build()
//
//            val httpRequest = Request.Builder()
//                .url(resource)
//                .post(requestBody)
//                .build()
//
//            val client = OkHttpClient.Builder()
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .build()
//
//            val response = withContext(Dispatchers.IO) {
//                client.newCall(httpRequest).execute()
//            }
//
//            val responseBody = response.body?.string()
//            Log.d("SyncClientService", "Upload response: $responseBody")
//
//            if (responseBody != null) {
//                Gson().fromJson(responseBody, DeviceToServerResponse::class.java)
//            } else {
//                DeviceToServerResponse().apply {
//                    statusCode = 0
//                    error = "No response from server"
//                }
//            }
//
//        } catch (ex: Exception) {
//            Log.e("SyncClientService", "uploadImage exception: ${ex.message}", ex)
//            DeviceToServerResponse().apply {
//                statusCode = 0
//                error = ex.message ?: "Unknown error"
//            }
//        }
//    }
//
//    // ─── Device To Server (with syncVersion) ─────────────────────────
//    // C#: DeviceToServer(DeviceToServerRequest)
//    suspend fun deviceToServer(request: DeviceToServerRequest): DeviceToServerResponse {
//
//        val controller = "deviceToServer.do"
//
//        return try {
//            val resource = "$baseUrl/${request.userId}/${request.currentDeviceUdid}/$controller" +
//                    "?syncVersion=${request.syncVersion}"
//
//            val parameters = Gson().toJson(request.parameters)
//
//            // C#: RegexReplace — replace space between date and time with T
//            val paramSerialize = parameters.replace(
//                Regex("(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2})"),
//                "$1T$2"
//            ).ifEmpty { parameters }
//
//            Log.d("SyncClientService", "DeviceToServer URL: $resource")
//
//            val response = RestClientService.executePostRequestAsync(resource, paramSerialize)
//
//            Log.d("SyncClientService", "DeviceToServer response: $response")
//
//            if (response != null) {
//                Gson().fromJson(response, DeviceToServerResponse::class.java)
//            } else {
//                DeviceToServerResponse().apply {
//                    statusCode = 0
//                    error = "No response from server"
//                }
//            }
//
//        } catch (ex: Exception) {
//            Log.e("SyncClientService", "deviceToServer exception: ${ex.message}", ex)
//            DeviceToServerResponse().apply {
//                statusCode = 0
//                error = ex.message ?: "Unknown error"
//            }
//        }
//    }
//
//    // ─── Device To Server (simple — no syncVersion) ───────────────────
//    // C#: DeviceToServer(SimpleDeviceToServerRequest)
//    suspend fun deviceToServerSimple(request: SimpleDeviceToServerRequest): DeviceToServerResponse {
//
//        val controller = "deviceToServer.do"
//
//        return try {
//            val resource = "$baseUrl/${request.userId}/${request.currentDeviceUdid}/$controller"
//
//            val parameters = Gson().toJson(request.parameters)
//
//            // C#: RegexReplace — replace T with space (opposite direction)
//            val paramSerialize = parameters.replace(
//                Regex("(\\d{4}-\\d{2}-\\d{2})T(\\d{2}:\\d{2}:\\d{2})"),
//                "$1 $2"
//            ).ifEmpty { parameters }
//
//            Log.d("SyncClientService", "DeviceToServerSimple URL: $resource")
//
//            val response = RestClientService.executePostRequestAsync(resource, paramSerialize)
//
//            Log.d("SyncClientService", "DeviceToServerSimple response: $response")
//
//            if (response != null) {
//                Gson().fromJson(response, DeviceToServerResponse::class.java)
//            } else {
//                DeviceToServerResponse().apply {
//                    statusCode = 0
//                    error = "No response from server"
//                }
//            }
//
//        } catch (ex: Exception) {
//            Log.e("SyncClientService", "deviceToServerSimple exception: ${ex.message}", ex)
//            DeviceToServerResponse().apply {
//                statusCode = 0
//                error = ex.message ?: "Unknown error"
//            }
//        }
//    }
//
//    // ─── Download Company Assign To User With DB Gzip ─────────────────
//    // C#: DownloadCompanyAssignToUserWithDBGzip()
//    suspend fun downloadCompanyAssignToUserWithDBGzip(
//        request: DownloadCompanyAssignToUserWithDBGzipRequest,
//        dbPath: String,
//        databaseName: String,
//        isFreshInstall: Boolean,
//        assetCount: Int
//    ): DownloadCompanyAssignToUserWithDBGzipResponse {
//
//        val controller = "downloadCompanyAssignToUserWithDBGzip.do"
//        val tempDbFilename = "TempDB.zip"
//        val pathTempDBZip = "$dbPath/$tempDbFilename"
//        val tempDbExtracted = "$dbPath/Temp_$databaseName"
//        val finalDbPath = "$dbPath/$databaseName"
//
//        return try {
//            // Ensure directory exists
//            File(dbPath).mkdirs()
//
//            // Build URL — same as C#
//            val resource = StringBuilder()
//                .append("$baseUrl/${request.userId}/${request.currentDeviceUdid}/$controller")
//                .append("?syncVersion=1.3.0")
//                .append("&currentDeviceType=${request.currentDeviceType}")
//                .append("&companyId=${request.companyId}")
//                .append("&userLastUpdateTimeStamp=${if (assetCount == 0) "0" else request.userLastUpdateTimeStamp}")
//                .append("&userId=${request.userId}")
//                .append("&currentDeviceUdid=${request.currentDeviceUdid}")
//                .toString()
//
//            Log.d("SyncClientService", "DownloadDB URL: $resource")
//
//            // Download zip file
//            val client = OkHttpClient.Builder()
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(120, TimeUnit.SECONDS)
//                .writeTimeout(120, TimeUnit.SECONDS)
//                .build()
//
//            val httpRequest = Request.Builder()
//                .url(resource)
//                .post("".toRequestBody("application/json".toMediaType()))
//                .build()
//
//            withContext(Dispatchers.IO) {
//                client.newCall(httpRequest).execute().use { response ->
//                    response.body?.byteStream()?.use { inputStream ->
//                        FileOutputStream(pathTempDBZip).use { outputStream ->
//                            inputStream.copyTo(outputStream)
//                        }
//                    }
//                }
//            }
//
//            // Verify downloaded file
//            val fileInfo = File(pathTempDBZip)
//            if (!fileInfo.exists() || fileInfo.length() == 0L) {
//                throw Exception("Downloaded file is empty")
//            }
//
//            Log.d("SyncClientService", "Downloaded file size: ${fileInfo.length()} bytes")
//
//            // Delete existing temp extracted if exists
//            File(tempDbExtracted).takeIf { it.exists() }?.delete()
//
//            // Extract GZip — Android only (no iOS in Kotlin)
//            withContext(Dispatchers.IO) {
//                GZIPInputStream(FileInputStream(pathTempDBZip)).use { gzipStream ->
//                    FileOutputStream(tempDbExtracted).use { outputStream ->
//                        gzipStream.copyTo(outputStream)
//                    }
//                }
//            }
//
//            Log.d("SyncClientService", "Extracted DB to: $tempDbExtracted")
//
//            // Replace main DB if fresh install or no assets
//            if (assetCount == 0 || isFreshInstall) {
//                File(finalDbPath).takeIf { it.exists() }?.delete()
//                File(tempDbExtracted).copyTo(File(finalDbPath))
//                Log.d("SyncClientService", "Replaced main DB at: $finalDbPath")
//            }
//
//            // Cleanup temp zip
//            File(pathTempDBZip).takeIf { it.exists() }?.delete()
//
//            DownloadCompanyAssignToUserWithDBGzipResponse().apply {
//                statusCode = 1
//            }
//
//        } catch (ex: Exception) {
//            Log.e("SyncClientService", "downloadDB exception: ${ex.message}", ex)
//
//            // Always cleanup temp files
//            try { File(pathTempDBZip).takeIf { it.exists() }?.delete() } catch (_: Exception) {}
//
//            DownloadCompanyAssignToUserWithDBGzipResponse().apply {
//                statusCode = 0
//                error = ex.message ?: "Unknown error"
//            }
//        }
//    }
//
//    // ─── Get Assign Company List To User ─────────────────────────────
//    // C#: GetAssignCompanyListToUser()
//    suspend fun getAssignCompanyListToUser(
//        request: GetAssignCompanyListToUserRequest
//    ): GetAssignCompanyListToUserResponse {
//
//        val controller = "getAssignCompanyListToUser.do"
//
//        return try {
//            val resource = "$baseUrl/${request.userId}/${request.currentDeviceUdid}/$controller" +
//                    "?userId=${request.userId}" +
//                    "&currentDeviceType=${request.currentDeviceType}" +
//                    "&currentDeviceUdid=${request.currentDeviceUdid}"
//
//            Log.d("SyncClientService", "GetAssignCompany URL: $resource")
//
//            val response = RestClientService.executeSimpleGetRequestAsync(resource)
//
//            Log.d("SyncClientService", "GetAssignCompany response: $response")
//
//            if (response != null) {
//                Gson().fromJson(response, GetAssignCompanyListToUserResponse::class.java)
//            } else {
//                GetAssignCompanyListToUserResponse().apply {
//                    statusCode = 0
//                    error = "No response from server"
//                }
//            }
//
//        } catch (ex: Exception) {
//            Log.e("SyncClientService", "getAssignCompanyListToUser exception: ${ex.message}", ex)
//            GetAssignCompanyListToUserResponse().apply {
//                statusCode = 0
//                error = ex.message ?: "Unknown error"
//            }
//        }
//    }
//
//    // ─── Floor Sweep Data Transfer ────────────────────────────────────
//    // C#: FloorSweepDataTransfer()
//    suspend fun floorSweepDataTransfer(request: FloorSweepRequest): FloorSweepResponse {
//
//        val controller = "floorSweep.do"
//
//        return try {
//            // C#: replaces /ws with /sws for FloorSweep
//            val tempBaseUrl = baseUrl.replace("/ws", "")
//            val resource = "$tempBaseUrl/sws/$controller"
//
//            val parameters = Gson().toJson(request)
//
//            // C#: RegexReplace — replace space with T in datetime
//            val paramSerialize = parameters.replace(
//                Regex("(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2})"),
//                "$1T$2"
//            ).ifEmpty { parameters }
//
//            Log.d("SyncClientService", "FloorSweep URL: $resource")
//
//            val response = RestClientService.executePostRequestAsync(resource, paramSerialize)
//
//            Log.d("SyncClientService", "FloorSweep response: $response")
//
//            if (response != null) {
//                Gson().fromJson(response, FloorSweepResponse::class.java)
//            } else {
//                FloorSweepResponse().apply {
//                    statusCode = 0
//                    error = "No response received from server"
//                }
//            }
//
//        } catch (ex: Exception) {
//            Log.e("SyncClientService", "floorSweepDataTransfer exception: ${ex.message}", ex)
//            FloorSweepResponse().apply {
//                statusCode = 0
//                error = ex.message ?: "Unknown error"
//            }
//        }
//    }
//}