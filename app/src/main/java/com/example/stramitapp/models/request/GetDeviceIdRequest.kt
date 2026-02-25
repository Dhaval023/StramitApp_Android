package com.example.stramitapp.models.request

data class GetDeviceIdRequest(
    var currentDeviceType: String? = null,
    var currentDeviceUdid: String? = null,
    var deviceTokenId: String? = null
)