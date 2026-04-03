package com.example.stramitapp.common.API.Login.request

data class GetDeviceIdRequest(
    var currentDeviceType: String? = null,
    var currentDeviceUdid: String? = null,
    var deviceTokenId: String? = null
)