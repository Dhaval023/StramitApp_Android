package com.example.stramitapp.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stramitapp.utils.SecureStorage
import com.example.stramitapp.utils.StorageKeys

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val secureStorage = SecureStorage(application)

    private val _licenseKey = MutableLiveData<String>()
    val licenseKey: LiveData<String> = _licenseKey

    private val _rememberLicenseKey = MutableLiveData<Boolean>()
    val rememberLicenseKey: LiveData<Boolean> = _rememberLicenseKey

    private val _versionText = MutableLiveData<String>()
    val versionText: LiveData<String> = _versionText

    init {
        // Load saved values
        _licenseKey.value = secureStorage.get(StorageKeys.LICENSEE_KEY) ?: "77Y5HKIOI3YTA0G40NCL"
        _rememberLicenseKey.value = true // Default to true

        // App version text
        _versionText.value = "Test Stramit AsTrack Version 2.3"
    }

    fun onLicenseKeyChanged(value: String) {
        _licenseKey.value = value
    }

    fun onRememberChanged(value: Boolean) {
        _rememberLicenseKey.value = value
    }

    fun saveSettings(): Boolean {
        val key = licenseKey.value

        if (key.isNullOrEmpty()) {
            return false
        }

        if (rememberLicenseKey.value == true) {
            secureStorage.set(StorageKeys.LICENSEE_KEY, key)
        } else {
            // Logic for what happens when remember is false could be added here
        }

        return true
    }
}