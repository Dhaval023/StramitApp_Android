package com.example.stramitapp.services.API.Sync.response

data class DownloadCompanyAssignToUserWithDBGzipResponse(

    var statusCode: Int = 0,
    var error: String? = null,
    var success: String? = null,
    var databaseTimeStamp: String? = null,
    var list: String? = null
)