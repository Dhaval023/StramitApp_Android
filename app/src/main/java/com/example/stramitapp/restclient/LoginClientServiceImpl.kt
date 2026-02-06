package com.example.stramitapp.restclient

import com.example.stramitapp.model.GetDeviceIdRequest
import com.example.stramitapp.models.response.GetDeviceIdResponse
import com.example.stramitapp.models.request.GetLoginDetailsNewRequest
import com.example.stramitapp.models.response.GetLoginDetailsNewResponse
import com.example.stramitapp.security.ApiSettings
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class LoginClientServiceImpl(private val client: OkHttpClient, private val gson: Gson) : LoginClientService {

    private val BASE_URL = ApiSettings.BASE_URL

    override suspend fun getLoginDetails(request: GetLoginDetailsNewRequest): GetLoginDetailsNewResponse? = withContext(Dispatchers.IO) {
        try {
            val json = gson.toJson(request)
            val requestBody = json.toRequestBody("application/json".toMediaType())

            val httpRequest = Request.Builder()
                .url("${BASE_URL}/api/Login/GetLoginDetailsNew") 
                .post(requestBody)
                .build()

            val response = client.newCall(httpRequest).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                gson.fromJson(responseBody, GetLoginDetailsNewResponse::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getDeviceId(request: GetDeviceIdRequest): GetDeviceIdResponse? = withContext(Dispatchers.IO) {
        try {
            val json = gson.toJson(request)
            val requestBody = json.toRequestBody("application/json".toMediaType())

            val httpRequest = Request.Builder()
                .url("${BASE_URL}/api/Login/GetDeviceId")
                .post(requestBody)
                .build()

            val response = client.newCall(httpRequest).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                gson.fromJson(responseBody, GetDeviceIdResponse::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}