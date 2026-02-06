package com.example.stramitapp.services

import com.example.stramitapp.model.GetDeviceIdRequest
import com.example.stramitapp.model.User
import com.example.stramitapp.model.UserRepository
import com.example.stramitapp.models.request.GetLoginDetailsNewRequest
import com.example.stramitapp.restclient.LoginClientService
import com.example.stramitapp.security.AesBase64Wrapper
import com.example.stramitapp.utils.AppSettings
import com.example.stramitapp.utils.SecureStorage
import com.example.stramitapp.utils.StorageKeys

class AuthenticationService(
    private val clientService: LoginClientService,
    private val userRepository: UserRepository,
    private val secureStorage: SecureStorage
) {

    suspend fun loginOnline(
        username: String,
        password: String,
        isRememberCredentials: Boolean,
        isForceLogin: Boolean
    ): Int {

        val licenseeKey = fetchLicenseeKey() ?: return 0

        AppSettings.deviceId = getDeviceId()

        val request = GetLoginDetailsNewRequest(
            loginName = username,
            password = password,
            currentDeviceType = AppSettings.deviceType,
            currentDeviceUdid = AppSettings.deviceUdid,
            deviceId = AppSettings.deviceId,
            licennseeKey = licenseeKey,
            setForceFullAssign = isForceLogin
        )

        val result = clientService.getLoginDetails(request)

        if (result != null && result.statusCode == 1 && result.list.isNotEmpty()) {
            val loginDetails = result.list.first()

            val user = User(
                userId = loginDetails.userId,
                firstName = loginDetails.firstName,
                middleName = loginDetails.middleName,
                lastName = loginDetails.lastName,
                email = loginDetails.email,
                phone = loginDetails.phone,
                loginName = loginDetails.loginname,
                password = password,
                parentUserId = loginDetails.parentUserId,
                currentDeviceUdid = loginDetails.currentDeviceUdid,
                currentDeviceType = loginDetails.currentDeviceType,
                licenseeId = loginDetails.licenseeId,
                isActive = loginDetails.isActive
            )

            AppSettings.authenticatedUser = user

            if (isRememberCredentials) {
                secureStorage.set(StorageKeys.LOGGED_IN_USERNAME, user.loginName)
                secureStorage.set(StorageKeys.LOGGED_IN_PASSWORD, password)

                if (!userRepository.exists(user)) {
                    userRepository.insert(user)
                }
            }

            return 1
        }

        AppSettings.loginErrorMessage = result?.error
        return result?.statusCode ?: 0
    }

    suspend fun loginOffline(
        username: String,
        password: String,
        isRememberCredentials: Boolean
    ): Int {

        val users = userRepository.getUsers()
        val user = users.firstOrNull { it.loginName == username } ?: return 0

        // If encrypted in DB:
        // val decryptedPassword = AesBase64Wrapper.decodeAndDecrypt(user.password)

        if (user.password == password) {
            AppSettings.authenticatedUser = user

            if (isRememberCredentials) {
                secureStorage.set(StorageKeys.LOGGED_IN_USERNAME, username)
                secureStorage.set(StorageKeys.LOGGED_IN_PASSWORD, password)
            }
            return 1
        }
        return 0
    }

    fun isLicenseeKeyAvailable(): Boolean =
        !fetchLicenseeKey().isNullOrEmpty()

    private fun fetchLicenseeKey(): String? {
        return secureStorage.get(StorageKeys.LICENSEE_KEY)
            ?: AppSettings.licenseeKey
    }

    private suspend fun getDeviceId(): Int {
        val request = GetDeviceIdRequest(
            currentDeviceType = AppSettings.deviceType,
            currentDeviceUdid = AppSettings.deviceUdid,
            deviceTokenId = null
        )

        val result = clientService.getDeviceId(request)
        return if (result?.status == true && result.data != null) {
            result.data.id ?: 0
        } else {
            0
        }
    }
}