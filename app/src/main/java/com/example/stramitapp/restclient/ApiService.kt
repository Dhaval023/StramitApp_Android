package com.example.stramitapp.restclient

import com.example.stramitapp.model.GetDeviceIdRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {

    @GET
    suspend fun getRequest(@Url url: String): Response<ResponseBody>

    @POST
    suspend fun postRequest(@Url url: String, @Body body: Any): Response<ResponseBody>
    
    // Example of a specific POST request using your existing model
    @POST("getDeviceId")
    suspend fun getDeviceId(@Body request: GetDeviceIdRequest): Response<ResponseBody>
}
