package com.example.stramitapp.restclient

import android.util.Log
import com.example.stramitapp.model.GetDeviceIdRequest
import com.example.stramitapp.models.response.GetDeviceIdResponse
import com.example.stramitapp.models.request.GetLoginDetailsNewRequest
import com.example.stramitapp.models.response.GetLoginDetailsNewResponse
import com.example.stramitapp.security.ApiSettings
import com.example.stramitapp.restclient.LoginClientService
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
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()

            val response = client.newCall(httpRequest).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    try {
                        gson.fromJson(responseBody, GetLoginDetailsNewResponse::class.java)
                    } catch (e: Exception) {
                        Log.e("LoginClient", "Failed to parse login response: ${e.message}", e)
                        null
                    }
                } else {
                    Log.e("LoginClient", "Empty response body for login")
                    null
                }
            } else {
                Log.e("LoginClient", "Login failed with status: ${response.code} - ${response.message}")
                null
            }
        } catch (e: Exception) {
            Log.e("LoginClient", "Login API error: ${e.message}", e)
            null
        }
    }

    override suspend fun getDeviceId(request: GetDeviceIdRequest): GetDeviceIdResponse? = withContext(Dispatchers.IO) {
        try {
            val json = gson.toJson(request)
            val requestBody = json.toRequestBody("application/json".toMediaType())

            val httpRequest = Request.Builder()
                .url("${BASE_URL}/api/Login/GetDeviceId")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()

            val response = client.newCall(httpRequest).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    try {
                        gson.fromJson(responseBody, GetDeviceIdResponse::class.java)
                    } catch (e: Exception) {
                        Log.e("LoginClient", "Failed to parse device ID response: ${e.message}", e)
                        null
                    }
                } else {
                    Log.e("LoginClient", "Empty response body for device ID")
                    null
                }
            } else {
                Log.e("LoginClient", "GetDeviceId failed with status: ${response.code} - ${response.message}")
                null
            }
        } catch (e: Exception) {
            Log.e("LoginClient", "GetDeviceId API error: ${e.message}", e)
            null
        }
    }
}