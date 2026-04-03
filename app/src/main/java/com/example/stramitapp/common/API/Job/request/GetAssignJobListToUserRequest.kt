package com.example.stramitapp.services.API.Job.request

data class GetAssignJobListToUserRequest(
    val userId: Int,
    val currentDeviceType: String?,
    val currentDeviceUdid: String?,
    val serverGeneratedDeviceId: Int
)