package com.example.stramitapp.common.API.Sync.request

data class GetAssignCompanyListToUserRequest(

    val userId: Int = 0,
    val currentDeviceType: String? = null,
    val currentDeviceUdid: String? = null
)