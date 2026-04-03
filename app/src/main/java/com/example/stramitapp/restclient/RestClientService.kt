package com.example.stramitapp.restclient

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object RestClientService {

    val client = getUnsafeOkHttpClient()

    fun getUnsafeClient(): OkHttpClient {
        return client
    }

    private fun logSafe(tag: String, message: String, body: String?) {
        if (body == null) {
            Log.d(tag, "$message null")
        } else if (body.length > 1000) {
            Log.d(tag, "$message (truncated): ${body.take(1000)}... [Total Length: ${body.length}]")
        } else {
            Log.d(tag, "$message $body")
        }
    }

    suspend fun executeSimpleGetRequestAsync(request: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val httpRequest = Request.Builder()
                    .url(request)
                    .get()
                    .build()

                client.newCall(httpRequest).execute().use { response ->
                    val responseBody = response.body?.string()
                    Log.d("RestClientService", "Response code: ${response.code}, successful: ${response.isSuccessful}")
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
                val httpRequest = Request.Builder()
                    .url(request)
                    .post(requestBody)
                    .build()

                client.newCall(httpRequest).execute().use { response ->
                    val responseBody = response.body?.string()
                    Log.d("RestClientService", "Response code: ${response.code}, successful: ${response.isSuccessful}")
                    if (response.isSuccessful) {
                        responseBody
                    } else {
                        Log.e("RestClientService", "Failed! Code: ${response.code}")
                        null
                    }
                }
            } catch (e: Exception) {
                Log.e("RestClientService", "Exception during POST request to $request", e)
                null
            }
        }
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient {

        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
            }
        )

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
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
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    Log.d("RestClientService", "Response code: ${response.code}, successful: ${response.isSuccessful}")
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
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    Log.d("RestClientService", "Response code: ${response.code}, successful: ${response.isSuccessful}")
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
