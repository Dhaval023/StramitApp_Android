package com.example.stramitapp.models.request

data class ForgotPasswordNewRequest(
    val loginName: String,
    val licenseeKey: String
)