package com.example.stramitapp.services
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SecureStorageHelper {

    private const val FILE_NAME = "secure_prefs"

    private fun getPrefs(context: Context) =
        EncryptedSharedPreferences.create(
            context,
            FILE_NAME,
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun set(context: Context, key: String, value: String) {
        getPrefs(context).edit().putString(key, value).apply()
    }

    fun get(context: Context, key: String): String? {
        return getPrefs(context).getString(key, null)
    }

    fun has(context: Context, key: String): Boolean {
        return getPrefs(context).contains(key)
    }
}