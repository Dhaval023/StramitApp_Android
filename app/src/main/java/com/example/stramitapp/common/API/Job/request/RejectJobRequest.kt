package com.example.stramitapp.services.API.Job.request

data class RejectJobRequest(
    val userId: Int,
    val currentDeviceType: String?,
    val currentDeviceUdid: String?,
    val jobId: Int,
    val isDeclined: Boolean
)