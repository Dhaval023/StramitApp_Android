package com.example.stramitapp.common.API.Login.response

data class GetDeviceIdResponse(
    var statusCode: Int = 0,
    var error: String? = null,
    var list: List<com.example.stramitapp.common.API.Login.response.DeviceIdItem>? = null
)

data class DeviceIdItem(
    var deviceId: Int = 0
)