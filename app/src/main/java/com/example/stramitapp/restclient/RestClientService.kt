package com.example.stramitapp.restclient

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

object RestClientService {

    private val client = OkHttpClient()
    suspend fun executeSimpleGetRequestAsync(request: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("RestClientService", "Executing GET request to: $request")

                val httpRequest = Request.Builder()
                    .url(request)
                    .get()
                    .build()

                client.newCall(httpRequest).execute().use { response ->
                    val responseBody = response.body?.string()
                    Log.d("RestClientService", "Response code: ${response.code}, successful: ${response.isSuccessful}")
                    Log.d("RestClientService", "Response body: $responseBody")
                    if (response.isSuccessful) responseBody else null
                }
            } catch (e: Exception) {
                Log.e("RestClientService", "Exception during GET request to $request", e)
                null
            }
        }
    }
    suspend fun executeSimplePostRequestAsync(request: String, parameters: Map<String, String>): String? {
        return withContext(Dispatchers.IO) {
            try {
                val formBodyBuilder = FormBody.Builder()
                parameters.forEach { (key, value) ->
                    formBodyBuilder.add(key, value)
                }
                val requestBody = formBodyBuilder.build()
                Log.d("RestClientService", "Executing POST request to: $request with params: $parameters")

                val httpRequest = Request.Builder()
                    .url(request)
                    .post(requestBody)
                    .build()

                client.newCall(httpRequest).execute().use { response ->
                    val responseBody = response.body?.string()
                    Log.d("RestClientService", "Response code: ${response.code}, successful: ${response.isSuccessful}")
                    Log.d("RestClientService", "Response body: $responseBody") // <-- check this in logcat
                    if (response.isSuccessful) {
                        responseBody
                    } else {
                        Log.e("RestClientService", "Failed! Code: ${response.code}, Body: $responseBody") // <-- added
                        null
                    }
                }
            } catch (e: Exception) {
                Log.e("RestClientService", "Exception during POST request to $request", e)
                null
            }
        }
    }

    suspend fun executeGetRequestAsync(resource: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("RestClientService", "Executing GET request to: $resource")
                val request = Request.Builder()
                    .url(resource)
                    .get()
                    .build()

                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    Log.d("RestClientService", "Response code: ${response.code}, successful: ${response.isSuccessful}")
                    Log.d("RestClientService", "Response body: $responseBody")
                    if (response.isSuccessful) {
                        responseBody
                    } else null
                }
            } catch (e: Exception) {
                Log.e("RestClientService", "Exception during GET request to $resource", e)
                null
            }
        }
    }

    suspend fun executePostRequestAsync(resource: String, item: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("RestClientService", "Executing POST request to: $resource with body: $item")
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val body = item.toRequestBody(mediaType)

                val request = Request.Builder()
                    .url(resource)
                    .addHeader("Accept", "application/json")
                    .post(body)
                    .build()

                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    Log.d("RestClientService", "Response code: ${response.code}, successful: ${response.isSuccessful}")
                    Log.d("RestClientService", "Response body: $responseBody")
                    if (response.isSuccessful) {
                        responseBody
                    } else null
                }
            } catch (e: Exception) {
                Log.e("RestClientService", "Exception during POST request to $resource", e)
                null
            }
        }
    }

    suspend fun executeGetRequestAsync(request: Request): String? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("RestClientService", "Executing GET request: $request")
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    Log.d("RestClientService", "Response code: ${response.code}, successful: ${response.isSuccessful}")
                    Log.d("RestClientService", "Response body: $responseBody")
                    if (response.isSuccessful) {
                        responseBody
                    } else null
                }
            } catch (e: Exception) {
                Log.e("RestClientService", "Exception during GET request: $request", e)
                null
            }
        }
    }

    suspend fun executePostRequestAsync(request: Request): String? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("RestClientService", "Executing POST request: $request")
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    Log.d("RestClientService", "Response code: ${response.code}, successful: ${response.isSuccessful}")
                    Log.d("RestClientService", "Response body: $responseBody")
                    if (response.isSuccessful) {
                        responseBody
                    } else null
                }
            } catch (e: Exception) {
                Log.e("RestClientService", "Exception during POST request: $request", e)
                null
            }
        }
    }

    suspend fun getResponsePostRequestAsync(request: Request): Response? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("RestClientService", "Executing POST request for Response object: $request")
                val response = client.newCall(request).execute()
                Log.d("RestClientService", "Response code: ${response.code}, successful: ${response.isSuccessful}")
                response
            } catch (e: Exception) {
                Log.e("RestClientService", "Exception during POST request for Response: $request", e)
                null
            }
        }
    }

    fun cancelPendingRequests() {
        client.dispatcher.cancelAll()
    }
}