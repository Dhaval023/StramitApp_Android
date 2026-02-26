package com.example.stramitapp.services.API.response

data class GetDeviceIdResponse(
    var statusCode: Int = 0,
    var error: String? = null,
    var list: List<DeviceIdItem>? = null
)

data class DeviceIdItem(
    var deviceId: Int = 0
)