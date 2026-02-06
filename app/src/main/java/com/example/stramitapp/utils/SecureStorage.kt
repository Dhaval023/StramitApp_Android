package com.example.stramitapp.utils

import android.content.Context
import android.content.SharedPreferences

class SecureStorage(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)

    fun get(key: String): String? {
        return prefs.getString(key, null)
    }

    fun set(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}