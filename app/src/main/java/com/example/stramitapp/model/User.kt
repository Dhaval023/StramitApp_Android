package com.example.stramitapp.model

data class User(
    val userId: Int,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val loginName: String,
    val password: String,
    val parentUserId: Int,
    val currentDeviceUdid: String,
    val currentDeviceType: String,
    val licenseeId: Int,
    val isActive: Boolean
)