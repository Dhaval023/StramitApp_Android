package com.example.stramitapp.services.API.Sync.request

data class DownloadCompanyAssignToUserWithDBGzipRequest(

    val userId: Int = 0,
    val currentDeviceType: String? = null,
    val currentDeviceUdid: String? = null,
    val companyId: Int? = null,
    val userLastUpdateTimeStamp: String? = null,
    val syncVersion: String? = null
)