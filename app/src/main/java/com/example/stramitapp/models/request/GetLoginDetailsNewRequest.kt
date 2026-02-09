package com.example.stramitapp.models.request

data class GetLoginDetailsNewRequest(
    val loginName: String,
    val password: String,
    val currentDeviceType: String,
    val currentDeviceUdid: String,
    val deviceId: Int,
    val licenseeKey: String,
    val setForceFullAssign: Boolean
)