package com.example.stramitapp.services.API.Login.request

data class GetLoginDetailsNewRequest(
    var loginName: String? = null,
    var password: String? = null,
    var currentDeviceType: String? = null,
    var currentDeviceUdid: String? = null,
    var deviceId: Int = 0,
    var licennseeKey: String? = null,  // ✅ double nn
    var setForceFullAssign: Boolean = false
)