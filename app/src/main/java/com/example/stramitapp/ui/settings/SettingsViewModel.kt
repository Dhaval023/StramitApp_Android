package com.example.stramitapp.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stramitapp.models.Constants.StorageKeys
import com.zebra.rfid.api3.BuildConfig

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _licenseKey = MutableLiveData<String>()
    val licenseKey: LiveData<String> = _licenseKey

    private val _rememberLicenseKey = MutableLiveData<Boolean>()
    val rememberLicenseKey: LiveData<Boolean> = _rememberLicenseKey

    private val _versionText = MutableLiveData<String>()
    val versionText: LiveData<String> = _versionText

    init {
        val context = getApplication<Application>()
        // ✅ Load saved values on open
        _licenseKey.value = StorageKeys.getLicenseKey(context)
        _rememberLicenseKey.value = StorageKeys.getRememberCredentials(context)
        _versionText.value = "Stramit AsTrack Version ${BuildConfig.VERSION_NAME}"
    }

    fun onLicenseKeyChanged(value: String) {
        _licenseKey.value = value
    }

    fun onRememberChanged(value: Boolean) {
        _rememberLicenseKey.value = value
    }

    fun saveSettings(): Boolean {
        val key = _licenseKey.value?.trim()
        val context = getApplication<Application>()

        if (key.isNullOrEmpty()) return false

        val remember = _rememberLicenseKey.value ?: true

        StorageKeys.saveLicenseKey(context, key)
        StorageKeys.saveRememberCredentials(context, remember)

        // If remember is false — clear key so next launch prompts again
        if (!remember) {
            StorageKeys.clearLicenseKey(context)
        }

        return true
    }
}