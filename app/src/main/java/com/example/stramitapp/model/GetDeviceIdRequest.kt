package com.example.stramitapp.model

data class GetDeviceIdRequest(
    val currentDeviceType: String,
    val currentDeviceUdid: String,
    val deviceTokenId: String?
)