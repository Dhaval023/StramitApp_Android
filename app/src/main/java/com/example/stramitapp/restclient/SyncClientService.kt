package com.example.stramitapp.restclient

import android.util.Log
import com.example.stramitapp.model.New.SimpleDeviceToServerRequest
import com.example.stramitapp.model.Asset
import com.example.stramitapp.models.Database.AppDatabase
import com.example.stramitapp.common.API.Sync.request.DeviceToServerRequest
import com.example.stramitapp.common.API.Sync.response.DeviceToServerResponse
import com.example.stramitapp.common.API.Sync.response.GetAssignCompanyListToUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import java.io.*
import com.example.stramitapp.utilities.AppSettings
import com.example.stramitapp.models.Constants.ApiClient
import com.example.stramitapp.common.API.Sync.request.DownloadCompanyAssignToUserWithDBGzipRequest
import com.example.stramitapp.common.API.Sync.request.GetAssignCompanyListToUserRequest
import com.example.stramitapp.common.API.Sync.response.DownloadCompanyAssignToUserWithDBGzipResponse
import com.example.stramitapp.common.APIHelper
import com.example.stramitapp.common.API.FloorSweep.request.FloorSweepRequest
import com.example.stramitapp.common.API.FloorSweep.response.FloorSweepResponse
import com.example.stramitapp.model.DataObject.BaseDataObject

class SyncClientService : ApiClient() {
    private val gson: Gson = GsonBuilder()
        .addSerializationExclusionStrategy(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return f.name == "id" &&
                        f.declaringClass == BaseDataObject::class.java
            }
            override fun shouldSkipClass(clazz: Class<*>): Boolean = false
        })
        .addDeserializationExclusionStrategy(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return f.name == "id" &&
                        f.declaringClass == BaseDataObject::class.java
            }
            override fun shouldSkipClass(clazz: Class<*>): Boolean = false
        })
        .create()

    private val httpClient = RestClientService.getUnsafeClient()

    suspend fun uploadImage(
        item: Asset,
        request: SimpleDeviceToServerRequest
    ): DeviceToServerResponse = withContext(Dispatchers.IO) {
        val result = DeviceToServerResponse()
        controller = "uploadAssetImage.do"

        try {
            val resource = "${baseUrl.trimEnd('/')}/${request.userId}/${request.currentDeviceUdid}/$controller"
            val file = File("${AppSettings.pathAssetNewImages}${item.barcode}.jpg")

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fileData", file.name, file.asRequestBody("image/jpeg".toMediaType()))
                .addFormDataPart("assetId", item.assetId.toString())
                .addFormDataPart("deviceId", item.deviceId.toString())
                .addFormDataPart("barcode", item.barcode.toString())
                .addFormDataPart("companyId", item.companyId.toString())
                .build()

            val httpRequest = Request.Builder().url(resource).post(requestBody).build()
            val response = httpClient.newCall(httpRequest).execute()
            val body = response.body?.string()
            return@withContext if (body != null) {
                gson.fromJson(body, DeviceToServerResponse::class.java)
            } else {
                result
            }
        } catch (ex: Exception) {
            Log.e("SyncClientService", "uploadImage Exception: ${ex.message}", ex)
            result.apply { statusCode = 0; error = ex.message }
        }
    }

    suspend fun deviceToServer(
        request: DeviceToServerRequest
    ): DeviceToServerResponse = withContext(Dispatchers.IO) {
        val result = DeviceToServerResponse()
        controller = "deviceToServer.do"

        try {
            val resource = "${baseUrl.trimEnd('/')}/${request.userId}/${request.currentDeviceUdid}/$controller" +
                    "?syncVersion=${request.syncVersion}"

            val parameters = gson.toJson(request.parameters)
            val paramSerialize = APIHelper.regexReplace(parameters, "JsonDateTime", " ", "T")
                .takeIf { it.isNotEmpty() } ?: parameters

            val response = RestClientService.executePostRequestAsync(resource, paramSerialize)
            if (response != null) gson.fromJson(response, DeviceToServerResponse::class.java)
            else result

        } catch (ex: Exception) {
            Log.e("SyncClientService", "deviceToServer Exception: ${ex.message}", ex)
            result.apply { statusCode = 0; error = ex.message }
        }
    }

    suspend fun deviceToServer(
        request: SimpleDeviceToServerRequest
    ): DeviceToServerResponse = withContext(Dispatchers.IO) {
        val result = DeviceToServerResponse()
        controller = "deviceToServer.do"

        try {
            val resource = "${baseUrl.trimEnd('/')}/${request.userId}/${request.currentDeviceUdid}/$controller"
            val parameters = gson.toJson(request.parameters)
            val paramSerialize = APIHelper.regexReplace(parameters, "JsonDateTime", "T", " ")
                .takeIf { it.isNotEmpty() } ?: parameters

            val response = RestClientService.executePostRequestAsync(resource, paramSerialize)
            if (response != null) gson.fromJson(response, DeviceToServerResponse::class.java)
            else result

        } catch (ex: Exception) {
            Log.e("SyncClientService", "deviceToServer (simple) Exception: ${ex.message}", ex)
            result.apply { statusCode = 0; error = ex.message }
        }
    }

    suspend fun downloadCompanyAssignToUserWithDBGzip(
        request: DownloadCompanyAssignToUserWithDBGzipRequest
    ): DownloadCompanyAssignToUserWithDBGzipResponse = withContext(Dispatchers.IO) {

        val result = DownloadCompanyAssignToUserWithDBGzipResponse()
        controller = "downloadCompanyAssignToUserWithDBGzip.do"

        val dbPath = AppSettings.pathDatabase
        val pathTempDBZip = File(dbPath, "TempDB.zip")
        val tempDbExtracted = File(dbPath, "Temp_${AppSettings.databaseName}")
        val finalDbPath = File(dbPath, AppSettings.databaseName)

        try {
            File(dbPath).mkdirs()

            val assetCount = try {
                AppDatabase.getInstance().assetDao().count()
            } catch (ex: Exception) {
                Log.w("SyncClientService", "assetCount failed, defaulting to 0: ${ex.message}")
                0
            }

            val resource = buildString {
                append("${baseUrl.trimEnd('/')}/${request.userId}/${request.currentDeviceUdid}/$controller?syncVersion=1.3.0")
                append("&currentDeviceType=${request.currentDeviceType}")
                append("&companyId=${request.companyId}")
                append("&userLastUpdateTimeStamp=${if (assetCount == 0) "0" else request.userLastUpdateTimeStamp}")
                append("&userId=${request.userId}")
                append("&currentDeviceUdid=${request.currentDeviceUdid}")
            }

            if (pathTempDBZip.exists()) pathTempDBZip.delete()

            val emptyBody = "".toRequestBody("application/json".toMediaType())
            val httpRequest = Request.Builder().url(resource).post(emptyBody).build()

            httpClient.newCall(httpRequest).execute().use { response ->
                response.body?.byteStream()?.use { input ->
                    FileOutputStream(pathTempDBZip).use { output -> input.copyTo(output) }
                }
            }

            Log.d("SyncClientService", "Downloaded file size: ${pathTempDBZip.length()}")

            if (pathTempDBZip.length() <= 0L) throw Exception("Downloaded file is empty")

            if (tempDbExtracted.exists()) tempDbExtracted.delete()

            // Detect whether the downloaded file is GZIP or ZIP
            val fis = FileInputStream(pathTempDBZip)
            val header = ByteArray(2)
            fis.read(header)
            fis.close()

            val isGzip = header[0] == 0x1f.toByte() && header[1] == 0x8b.toByte()

            if (isGzip) {
                Log.d("SyncClientService", "Detected GZIP file")
                FileInputStream(pathTempDBZip).use { fis2 ->
                    GzipCompressorInputStream(BufferedInputStream(fis2)).use { gzip ->
                        FileOutputStream(tempDbExtracted).use { out ->
                            gzip.copyTo(out)
                        }
                    }
                }
            } else {
                Log.d("SyncClientService", "Detected ZIP file")
                ZipArchiveInputStream(BufferedInputStream(FileInputStream(pathTempDBZip))).use { zis ->
                    var entry = zis.nextZipEntry
                    while (entry != null) {
                        if (!entry.isDirectory) {
                            FileOutputStream(tempDbExtracted).use { out ->
                                zis.copyTo(out)
                            }
                            break
                        }
                        entry = zis.nextZipEntry
                    }
                }
            }

            if (assetCount == 0 || AppSettings.isFreshInstall.equals("Yes", ignoreCase = true)) {
                if (finalDbPath.exists()) finalDbPath.delete()
                tempDbExtracted.copyTo(finalDbPath, overwrite = true)
            }

            if (pathTempDBZip.exists()) pathTempDBZip.delete()

            result.statusCode = 1

        } catch (ex: Exception) {
            result.statusCode = 0
            result.error = ex.message
            Log.e("SyncClientService", "downloadCompanyAssignToUserWithDBGzip error: ${ex.message}", ex)
        } finally {
            try {
                if (pathTempDBZip.exists()) pathTempDBZip.delete()
            } catch (e: Exception) {
                Log.w("SyncClientService", "Cleanup failed: ${e.message}")
            }
        }

        result
    }
    suspend fun getAssignCompanyListToUser(
        request: GetAssignCompanyListToUserRequest
    ): GetAssignCompanyListToUserResponse = withContext(Dispatchers.IO) {

        val result = GetAssignCompanyListToUserResponse()
        controller = "getAssignCompanyListToUser.do"

        try {
            val resource = "${baseUrl.trimEnd('/')}/${request.userId}/${request.currentDeviceUdid}/$controller" +
                    "?userId=${request.userId}" +
                    "&currentDeviceType=${request.currentDeviceType}" +
                    "&currentDeviceUdid=${request.currentDeviceUdid}"

            val response = RestClientService.executeSimpleGetRequestAsync(resource)
            if (response != null) gson.fromJson(response, GetAssignCompanyListToUserResponse::class.java)
            else result

        } catch (ex: Exception) {
            Log.e("SyncClientService", "getAssignCompanyListToUser Exception: ${ex.message}", ex)
            result.apply { statusCode = 0; error = ex.message }
        }
    }

    suspend fun floorSweepDataTransfer(
        request: FloorSweepRequest
    ): FloorSweepResponse = withContext(Dispatchers.IO) {

        val result = FloorSweepResponse()
        controller = "floorSweep.do"

        try {
            val tempBaseUrl = baseUrl.replace("/ws", "").removeSuffix("/")
            val resource = "$tempBaseUrl/sws/$controller"

            val parameters = gson.toJson(request)
            val paramSerialize = APIHelper.regexReplace(parameters, "JsonDateTime", " ", "T")
                .takeIf { it.isNotEmpty() } ?: parameters

            val response = RestClientService.executePostRequestAsync(resource, paramSerialize)
            if (response != null) gson.fromJson(response, FloorSweepResponse::class.java)
            else result.apply { statusCode = 0; error = "No response received from server" }

        } catch (ex: Exception) {
            Log.e("SyncClientService", "floorSweepDataTransfer Exception: ${ex.message}", ex)
            result.apply { statusCode = 0; error = ex.message }
        }
    }

    private fun isAndroid(): Boolean =
        System.getProperty("java.vm.name") == "Dalvik"
}