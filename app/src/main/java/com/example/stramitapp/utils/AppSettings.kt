package com.example.stramitapp.utils

import com.example.stramitapp.model.User

object AppSettings {
    var deviceId: Int = 0
    const val deviceType: String = "Android"
    var deviceUdid: String = ""
    var authenticatedUser: User? = null
    var loginErrorMessage: String? = null
    var licenseeKey: String? = null
}