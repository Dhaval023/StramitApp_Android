package com.example.stramitapp.services.API.Login.request

data class ForgotPasswordNewRequest(
    val loginName: String? = null,
    val licenseeKey: String? = null
)