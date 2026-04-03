package com.example.stramitapp.services

import com.example.stramitapp.common.API.Job.request.DownloadJobAssignToUserWithDBGzipRequest
import com.example.stramitapp.common.API.Job.request.GetAssignJobListToUserRequest
import com.example.stramitapp.common.API.Job.request.RejectJobRequest
import com.example.stramitapp.common.API.Job.request.SubmitJobRequest
import com.example.stramitapp.common.API.Job.response.DownloadJobAssignToUserWithDBGzipResponse
import com.example.stramitapp.common.API.Job.response.GetAssignJobListToUserResponse
import com.example.stramitapp.common.API.Job.response.RejectJobResponse
import com.example.stramitapp.common.API.Job.response.SubmitJobResponse
import com.example.stramitapp.common.APIHelper
import com.example.stramitapp.models.Constants.ApiClient
import com.example.stramitapp.utilities.AppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.zip.GZIPInputStream
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class PendingJobClientService : ApiClient() {

    private val gson = Gson()
    private val client = OkHttpClient()

    suspend fun getAssignJobListToUser(
        request: GetAssignJobListToUserRequest
    ): GetAssignJobListToUserResponse = withContext(Dispatchers.IO) {

        var result = GetAssignJobListToUserResponse(
            statusCode = 0,
            error = null,
            success = null,
            databaseTimeStamp = null,
            list = null
        )

        try {
           val Controller = "getAssignJobListToUser.do"

            val resource = "$baseUrl/${request.userId}/${request.currentDeviceUdid}/$Controller?" +
                    "userId=${request.userId}" +
                    "&currentDeviceType=${request.currentDeviceType}" +
                    "&currentDeviceUdid=${request.currentDeviceUdid}" +
                    "&serverGeneratedDeviceId=${request.serverGeneratedDeviceId}"

            val httpRequest = Request.Builder().url(resource).get().build()
            val response = client.newCall(httpRequest).execute()

            if (response.isSuccessful) {
                val body = response.body?.string()
                if (!body.isNullOrEmpty()) {
                    result = gson.fromJson(body, GetAssignJobListToUserResponse::class.java)
                }
            }

        } catch (ex: Exception) {
            result = result.copy(
                statusCode = 0,
                error = ex.message
            )
        }

        result
    }

    suspend fun downloadJobAssignToUserWithDBGzip(
        request: DownloadJobAssignToUserWithDBGzipRequest
    ): DownloadJobAssignToUserWithDBGzipResponse = withContext(Dispatchers.IO) {

        var result = DownloadJobAssignToUserWithDBGzipResponse(
            statusCode = 0,
            error = null,
            success = null,
            databaseTimeStamp = null,
            list = null
        )

        try {
         val  Controller = "downloadJobAssignToUserWithDBGzip.do"

            val url = StringBuilder()
                .append("$baseUrl/${request.userId}/${request.currentDeviceUdid}/$Controller?syncVersion=1.3.0")
                .append("&userId=${request.userId}")
                .append("&currentDeviceType=${request.currentDeviceType}")
                .append("&currentDeviceUdid=${request.currentDeviceUdid}")
                .append("&jobId=${request.jobId}")
                .append("&userLastUpdateTimeStamp=${request.userLastUpdateTimeStamp}")
                .append("&setForceFullDoownload=${request.setForceFullDoownload}")
                .toString()

            val httpRequest = Request.Builder()
                .url(url)
                .post(FormBody.Builder().build())
                .build()

            val response = client.newCall(httpRequest).execute()

            val dbPath = AppSettings.pathDatabase
            val tempFile = File(dbPath, "TempDB.db")

            response.body?.byteStream()?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }

            if (tempFile.length() > 0) {
                result = result.copy(statusCode = 1)

                val outputFile = File(dbPath, "Temp_${AppSettings.databaseName}")

                GZIPInputStream(tempFile.inputStream()).use { gzip ->
                    FileOutputStream(outputFile).use { out ->
                        gzip.copyTo(out)
                    }
                }
            }

            tempFile.delete()

        } catch (ex: Exception) {
            result = result.copy(
                statusCode = 0,
                error = ex.message
            )

            File(AppSettings.pathDatabase, "TempDB.db").delete()
        }

        result
    }


    suspend fun submitJob(
        request: SubmitJobRequest
    ): SubmitJobResponse = withContext(Dispatchers.IO) {

        var result = SubmitJobResponse(
            statusCode = 0,
            error = null,
            success = null,
            databaseTimeStamp = null,
            list = null
        )

        try {
            val controller = "submitJob.do"

            val requestUrl =
                "$baseUrl/${request.userId}/${request.currentDeviceUdid}/${request.jobId}/${request.userId}/${AppSettings.deviceId}/$controller?syncVersion=1.3.0"

            var json = gson.toJson(request.tblJobAssetTrackInfo)

            val replaced = APIHelper.regexReplace(json, "JsonDateTime", " ", "T")
            if (!replaced.isNullOrEmpty()) {
                json = replaced
            }

            val body = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                json
            )

            val httpRequest = Request.Builder()
                .url(requestUrl)
                .post(body)
                .build()

            val response = client.newCall(httpRequest).execute()

            if (response.isSuccessful) {
                val resBody = response.body?.string()
                if (!resBody.isNullOrEmpty()) {
                    result = gson.fromJson(resBody, SubmitJobResponse::class.java)
                }
            }

        } catch (ex: Exception) {
            result = result.copy(
                statusCode = 0,
                error = ex.message
            )
        }

        result
    }

    suspend fun rejectJob(
        request: RejectJobRequest
    ): RejectJobResponse = withContext(Dispatchers.IO) {

        var result = RejectJobResponse(
            statusCode = 0,
            error = null,
            success = null,
            databaseTimeStamp = null,
            list = null
        )

        try {
            val url = StringBuilder()
                .append("$baseUrl/${request.userId}/${request.currentDeviceUdid}/setJobDecline.do?")
                .append("userId=${request.userId}")
                .append("&jobId=${request.jobId}")
                .append("&currentDeviceType=${request.currentDeviceType}")
                .append("&currentDeviceUdid=${request.currentDeviceUdid}")
                .append("&isDecline=${request.isDeclined}")
                .toString()

            val httpRequest = Request.Builder().url(url).get().build()
            val response = client.newCall(httpRequest).execute()

            if (response.isSuccessful) {
                val body = response.body?.string()
                if (!body.isNullOrEmpty()) {
                    result = gson.fromJson(body, RejectJobResponse::class.java)
                }
            }

        } catch (ex: Exception) {
            result = result.copy(
                statusCode = 0,
                error = ex.message
            )
        }

        result
    }
}