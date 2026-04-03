package com.example.stramitapp.restclient

import android.util.Log
import com.example.stramitapp.models.Constants.ApiClient
import com.example.stramitapp.common.API.Login.request.ForgotPasswordNewRequest
import com.example.stramitapp.common.API.Login.request.GetDeviceIdRequest
import com.example.stramitapp.common.API.Login.request.GetLoginDetailsNewRequest
import com.example.stramitapp.common.API.Login.response.ForgotPasswordNewResponse
import com.example.stramitapp.common.API.Login.response.GetDeviceIdResponse
import com.example.stramitapp.common.API.Login.response.GetLoginDetailsNewResponse
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

    @GET("ForgotPassword_new.do")
    suspend fun forgotPassword(
        @Query("loginName")   loginName: String,
        @Query("licenseeKey") licenseeKey: String
    ): Response<ForgotPasswordNewResponse>
}

class LoginClientService : ApiClient() {

    private val apiService: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }

    suspend fun getLoginDetails(request:GetLoginDetailsNewRequest): GetLoginDetailsNewResponse {
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
                response.body() ?: GetLoginDetailsNewResponse()
                    .apply {
                    statusCode = 0
                    error = "Empty response body."
                }
            } else {
                GetLoginDetailsNewResponse()
                    .apply {
                    error = response.message()
                }
            }
        } catch (ex: Exception) {
            Log.e("LoginClientService", "getLoginDetails failed", ex)
            GetLoginDetailsNewResponse()
                .apply {
                statusCode = 0
                error = ex.message ?: "An unknown error occurred during login."
            }
        }
    }

    suspend fun forgotPassword(request: ForgotPasswordNewRequest): ForgotPasswordNewResponse {
        return try {
            val response = apiService.forgotPassword(
                loginName   = request.loginName   ?: "",
                licenseeKey = request.licenseeKey ?: ""
            )

            if (response.isSuccessful) {
                response.body() ?: ForgotPasswordNewResponse()
                    .apply {
                    statusCode = 0
                    error      = "Empty response body."
                }
            } else {
                ForgotPasswordNewResponse()
                    .apply {
                    statusCode = 0
                    error      = response.message()
                }
            }
        } catch (ex: Exception) {
            Log.e("LoginClientService", "forgotPassword failed", ex)
            ForgotPasswordNewResponse()
                .apply {
                statusCode = 0
                error      = ex.message ?: "An unknown error occurred."
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