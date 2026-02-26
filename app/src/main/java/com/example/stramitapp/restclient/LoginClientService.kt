package com.example.stramitapp.restclient

import android.util.Log
import com.example.stramitapp.models.Constants.ApiClient
import com.example.stramitapp.services.API.request.GetDeviceIdRequest
import com.example.stramitapp.services.API.request.GetLoginDetailsNewRequest
import com.example.stramitapp.services.API.response.GetDeviceIdResponse
import com.example.stramitapp.services.API.response.GetLoginDetailsNewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginApiService {
    @GET("getLoginDetails_new.do")
    suspend fun getLoginDetails(
        @Query("loginName") loginName: String,
        @Query("password") password: String,
        @Query("currentDeviceType") currentDeviceType: String,
        @Query("currentDeviceUdid") currentDeviceUdid: String,
        @Query("deviceId") deviceId: String,
        @Query("licennseeKey") licenseeKey: String,
        @Query("setForceFullAssign") setForceFullAssign: Boolean? = null
    ): Response<GetLoginDetailsNewResponse>

    @GET("getDeviceId.do")
    suspend fun getDeviceId(
        @Query("currentDeviceType") currentDeviceType: String,
        @Query("currentDeviceUdid") currentDeviceUdid: String
    ): Response<GetDeviceIdResponse>
}

class LoginClientService : ApiClient() {

    private val apiService: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }

    suspend fun getLoginDetails(
        request: GetLoginDetailsNewRequest
    ): GetLoginDetailsNewResponse {
        return try {
            val response = apiService.getLoginDetails(
                loginName = request.loginName ?: "",
                password = request.password ?: "",
                currentDeviceType = request.currentDeviceType ?: "",
                currentDeviceUdid = request.currentDeviceUdid ?: "",
                deviceId = request.deviceId.toString(),
                licenseeKey = request.licennseeKey ?: "",
                setForceFullAssign = if (request.setForceFullAssign) true else null
            )

            if (response.isSuccessful) {
                response.body() ?: GetLoginDetailsNewResponse().apply {
                    statusCode = 0
                    error = "Empty response body."
                }
            } else {
                GetLoginDetailsNewResponse().apply {
                    statusCode = response.code()
                    error = response.message()
                }
            }
        } catch (ex: Exception) {
            Log.e("LoginClientService", "getLoginDetails failed", ex)
            GetLoginDetailsNewResponse().apply {
                statusCode = 0
                error = ex.message ?: "An unknown error occurred during login."
            }
        }
    }

    suspend fun getDeviceId(request: GetDeviceIdRequest): GetDeviceIdResponse? {
        return try {
            val response = apiService.getDeviceId(
                currentDeviceType = request.currentDeviceType ?: "",
                currentDeviceUdid = request.currentDeviceUdid ?: ""
            )

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (ex: Exception) {
            Log.e("LoginClientService", "getDeviceId failed", ex)
            null
        }
    }
}