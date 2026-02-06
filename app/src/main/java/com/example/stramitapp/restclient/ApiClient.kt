package com.example.stramitapp.restclient

import com.example.stramitapp.utils.ApiSettings

open class ApiClient {

    protected val baseUrl: String
    protected var controller: String? = null

    init {
        baseUrl = "${ApiSettings.SCHEME}://${ApiSettings.HOST}/${ApiSettings.ROOT}"
    }
}