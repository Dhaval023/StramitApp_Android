package com.example.stramitapp.services

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.example.stramitapp.models.Constants.StorageKeys
import com.example.stramitapp.models.request.GetDeviceIdRequest
import com.example.stramitapp.models.request.GetLoginDetailsNewRequest
import com.example.stramitapp.restclient.LoginClientService

class AuthenticationService(private val context: Context) {

    private val clientService = LoginClientService()
    var authenticatedUser: AuthenticatedUser? = null

    var loginErrorMessage: String = ""

    suspend fun loginOnline(
        username: String,
        password: String,
        isRememberCredentials: Boolean,
        isForceLogin: Boolean
    ): Int {
        loginErrorMessage = ""

        val licenseKey = fetchLicenseKey()
        if (licenseKey.isBlank()) {
            loginErrorMessage = "License key not configured"
            return 0
        }

        return try {
            val deviceId = getDeviceId()
            Log.d("AuthService", "DeviceId: $deviceId")

            val androidId = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            ) ?: "unknown"

            val request = GetLoginDetailsNewRequest(
                loginName         = username,
                password          = password,
                currentDeviceType = "Android",
                currentDeviceUdid = androidId,
                deviceId          = deviceId,
                licennseeKey      = licenseKey, // ✅ To match C# version
                setForceFullAssign = isForceLogin
            )

            val result = clientService.getLoginDetails(request)

            Log.d("AuthService", "Login Result: statusCode=${result.statusCode}, error=${result.error}, list=${result.list}")

            if (result.statusCode == 1) {
                val loginDetail = result.list?.firstOrNull()
                if (loginDetail != null) {
                    authenticatedUser = AuthenticatedUser(
                        userId            = loginDetail.userId,
                        firstName         = loginDetail.firstName,
                        middleName        = loginDetail.middleName,
                        lastName          = loginDetail.lastName,
                        email             = loginDetail.email,
                        phone             = loginDetail.phone,
                        loginName         = loginDetail.loginname,
                        password          = password,
                        parentUserId      = loginDetail.parentUserId,
                        currentDeviceUdid = loginDetail.currentDeviceUdid,
                        currentDeviceType = loginDetail.currentDeviceType,
                        licenseeId        = loginDetail.licenseeId,
                        isActive          = loginDetail.isActive
                    )
                }
                if (isRememberCredentials) {
                    StorageKeys.saveUsername(context, authenticatedUser?.loginName ?: username)
                    StorageKeys.savePassword(context, password)
                }
                1
            } else {
                loginErrorMessage = result.error ?: "Unknown login error"
                result.statusCode
            }

        } catch (e: Exception) {
            Log.e("AuthService", "loginOnline exception: ${e.message}", e)
            loginErrorMessage = e.message ?: "An unknown error occurred during login."
            0
        }
    }

    suspend fun loginOffline(
        username: String,
        password: String,
        isRememberCredentials: Boolean
    ): Int {
        loginErrorMessage = ""

        val savedUsername = StorageKeys.getUsername(context)
        val savedPassword = StorageKeys.getPassword(context)

        if (savedUsername.isBlank() || savedPassword.isBlank()) return 0

        return if (savedUsername == username && savedPassword == password) {
            authenticatedUser = AuthenticatedUser(
                loginName = username,
                password  = password
            )
            if (isRememberCredentials) {
                StorageKeys.saveUsername(context, username)
                StorageKeys.savePassword(context, password)
            }
            1
        } else {
            loginErrorMessage = "Invalid username or password"
            0
        }
    }

    fun isLicenseeKeyAvailable(): Boolean = fetchLicenseKey().isNotBlank()

    private fun fetchLicenseKey(): String = StorageKeys.getLicenseKey(context)

    private suspend fun getDeviceId(): Int {
        return try {
            val androidId = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            ) ?: "unknown"

            val request = GetDeviceIdRequest(
                currentDeviceType = "Android",
                currentDeviceUdid = androidId,
                deviceTokenId     = ""
            )

            val result = clientService.getDeviceId(request)

            if (result != null && result.statusCode == 1) {
                result.list?.firstOrNull()?.deviceId ?: 0
            } else {
                Log.w("AuthService", "GetDeviceId failed: ${result?.error}")
                0
            }
        } catch (e: Exception) {
            Log.e("AuthService", "GetDeviceId exception: ${e.message}", e)
            0
        }
    }

    fun logout() {
        StorageKeys.savePassword(context, "")
        StorageKeys.saveSelectedSystem(context, "")
        StorageKeys.saveSelectedCompany(context, "")
        StorageKeys.saveSelectedLocation(context, "")
        authenticatedUser = null
        loginErrorMessage = ""
    }
}

data class AuthenticatedUser(
    val userId: Int = 0,
    val firstName: String? = null,
    val middleName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val loginName: String? = null,
    val password: String? = null,
    val parentUserId: Int = 0,
    val currentDeviceUdid: String? = null,
    val currentDeviceType: Int? = null,
    val licenseeId: Int? = null,
    val isActive: Int = 0
)