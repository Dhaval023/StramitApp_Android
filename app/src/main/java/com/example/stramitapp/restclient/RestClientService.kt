package com.example.stramitapp.restclient

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object RestClientService {

    private val client = OkHttpClient()

    suspend fun executeSimpleGetRequest(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        return try {
            client.newCall(request).execute().use {
                if (it.isSuccessful) it.body?.string() else null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun executePostRequest(url: String, json: String): String? {
        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        return try {
            client.newCall(request).execute().use {
                if (it.isSuccessful) it.body?.string() else null
            }
        } catch (e: Exception) {
            null
        }
    }
}