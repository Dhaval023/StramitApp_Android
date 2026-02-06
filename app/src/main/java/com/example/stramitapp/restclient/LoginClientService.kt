package com.example.stramitapp.restclient

import com.example.stramitapp.model.GetDeviceIdRequest
import com.example.stramitapp.models.response.GetDeviceIdResponse
import com.example.stramitapp.models.request.GetLoginDetailsNewRequest
import com.example.stramitapp.models.response.GetLoginDetailsNewResponse

interface LoginClientService {
    suspend fun getLoginDetails(request: GetLoginDetailsNewRequest): GetLoginDetailsNewResponse?
    suspend fun getDeviceId(request: GetDeviceIdRequest): GetDeviceIdResponse?
}