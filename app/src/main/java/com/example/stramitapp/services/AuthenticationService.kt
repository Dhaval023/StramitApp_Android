package com.example.stramitapp.services

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.example.stramitapp.model.User
import com.example.stramitapp.models.Constants.StorageKeys
import com.example.stramitapp.models.Database.AppDatabase
import com.example.stramitapp.services.API.request.GetDeviceIdRequest
import com.example.stramitapp.services.API.request.GetLoginDetailsNewRequest
import com.example.stramitapp.restclient.LoginClientService
import com.example.stramitapp.utilities.AppSettings

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
                loginName          = username,
                password           = password,
                currentDeviceType  = "Android",
                currentDeviceUdid  = androidId,
                deviceId           = deviceId,
                licennseeKey       = licenseKey,
                setForceFullAssign = isForceLogin
            )

            val result = clientService.getLoginDetails(request)
            Log.d("AuthService", "Login Result: statusCode=${result.statusCode}, error=${result.error}")

            if (result.statusCode == 1L) {
                val loginDetail = result.list?.firstOrNull()
                if (loginDetail != null) {

                    // ── 1. Build AuthenticatedUser (in-memory DTO) ────────────
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

                    // ── 2. FIX: Set AppSettings.authenticatedUser ─────────────
                    // This is what SyncService reads. Was never set — caused NPE.
                    AppSettings.authenticatedUser = authenticatedUser!!.toUser()

                    // ── 3. FIX: Save User to Room tbl_user ────────────────────
                    // SyncService.forceSync_internal() calls db.userDao().getFirstUser()
                    // as a fallback. Without this insert it always returned null.
                    try {
                        val db = AppDatabase.getInstance()
                        db.userDao().insert(authenticatedUser!!.toUser())
                        Log.d("AuthService", "User saved to Room: userId=${loginDetail.userId}")
                    } catch (e: Exception) {
                        // Non-fatal — AppSettings.authenticatedUser is already set above
                        Log.w("AuthService", "Could not save user to Room: ${e.message}")
                    }
                }

                if (isRememberCredentials) {
                    StorageKeys.saveUsername(context, authenticatedUser?.loginName ?: username)
                    StorageKeys.savePassword(context, password)
                }
                1

            } else {
                loginErrorMessage = result.error ?: "Unknown login error"
                result.statusCode.toInt()
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

            // Also restore AppSettings from Room on offline login
            try {
                val db = AppDatabase.getInstance()
                val userFromDb = db.userDao().getFirstUser()
                if (userFromDb != null) {
                    AppSettings.authenticatedUser = userFromDb
                }
            } catch (e: Exception) {
                Log.w("AuthService", "Offline login: could not load user from Room: ${e.message}")
            }

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

    fun logout() {
        StorageKeys.savePassword(context, "")
        StorageKeys.saveSelectedSystem(context, "")
        StorageKeys.saveSelectedCompany(context, "")
        StorageKeys.saveSelectedLocation(context, "")
        AppSettings.authenticatedUser = null   // clear from AppSettings too
        authenticatedUser = null
        loginErrorMessage = ""
    }

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

            if (result != null && result.statusCode.toLong() == 1L) {
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
}

// ─────────────────────────────────────────────────────────────────────────────
// AuthenticatedUser DTO  (unchanged)
// ─────────────────────────────────────────────────────────────────────────────

data class AuthenticatedUser(
    val userId: Long = 0,
    val firstName: String? = null,
    val middleName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val loginName: String? = null,
    val password: String? = null,
    val parentUserId: Long = 0,
    val currentDeviceUdid: String? = null,
    val currentDeviceType: Long? = null,
    val licenseeId: Long? = null,
    val isActive: Long = 0
) {
    /**
     * Converts this login DTO into a Room [User] entity.
     * Called after successful login to persist the user and populate AppSettings.
     */
    fun toUser(): User = User().apply {
        userId            = this@AuthenticatedUser.userId.toInt()
        firstName         = this@AuthenticatedUser.firstName
        middleName        = this@AuthenticatedUser.middleName
        lastName          = this@AuthenticatedUser.lastName
        email             = this@AuthenticatedUser.email
        phone             = this@AuthenticatedUser.phone
        loginName         = this@AuthenticatedUser.loginName
        password          = this@AuthenticatedUser.password
        parentUserId      = this@AuthenticatedUser.parentUserId.toInt()
        currentDeviceUdid = this@AuthenticatedUser.currentDeviceUdid
        currentDeviceType = this@AuthenticatedUser.currentDeviceType?.toInt()
        licenseeId        = this@AuthenticatedUser.licenseeId?.toInt()
    }
}