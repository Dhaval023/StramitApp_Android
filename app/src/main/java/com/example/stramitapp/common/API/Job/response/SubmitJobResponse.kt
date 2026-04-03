package com.example.stramitapp.common.API.Job.response

data class SubmitJobResponse(
    val statusCode: Int,
    val error: String?,
    val success: String?,
    val databaseTimeStamp: String?,
    val list: String?
)