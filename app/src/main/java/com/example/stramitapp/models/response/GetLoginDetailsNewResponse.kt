package com.example.stramitapp.models.response

data class GetLoginDetailsNewResponse(
    val statusCode: Int,
    val list: List<LoginDetails>,
    val error: String
)

data class LoginDetails(
    val userId: Int,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val loginname: String,
    val parentUserId: Int,
    val currentDeviceUdid: String,
    val currentDeviceType: String,
    val licenseeId: Int,
    val isActive: Boolean
)