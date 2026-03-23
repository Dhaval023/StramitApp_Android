package com.example.stramitapp.services.API.response

import com.google.gson.annotations.SerializedName

class ForgotPasswordNewResponse {
    @SerializedName("statusCode") var statusCode: Int = 0
    @SerializedName("error")      var error: String? = null
}