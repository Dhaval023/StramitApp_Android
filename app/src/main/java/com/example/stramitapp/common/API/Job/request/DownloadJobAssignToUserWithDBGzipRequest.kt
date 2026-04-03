package com.example.stramitapp.common.API.Job.request

data class DownloadJobAssignToUserWithDBGzipRequest(
    val userId: Int,
    val currentDeviceType: String?,
    val currentDeviceUdid: String?,
    val jobId: Int,
    val userLastUpdateTimeStamp: String?,
    val setForceFullDoownload: Boolean
)