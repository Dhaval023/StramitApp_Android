package com.example.stramitapp.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.model.UserRepository
import com.example.stramitapp.restclient.LoginClientServiceImpl
import com.example.stramitapp.services.AuthenticationService
import com.example.stramitapp.utils.AppSettings
import com.example.stramitapp.utils.SecureStorage
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    private val authenticationService = AuthenticationService(
        LoginClientServiceImpl(OkHttpClient(), Gson()),
        UserRepository(),
        SecureStorage(application)
    )

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Please enter Username and Password")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val result = authenticationService.loginOnline(username, password, isRememberCredentials = false, isForceLogin = false)

            if (result == 1) {
                _uiState.value = LoginUiState.Success
            } else {
                // If login fails (result != 1), we still go to Success for testing as requested
                // In production, you would handle this based on isLicenseeKeyAvailable or show the error
                _uiState.value = LoginUiState.Success 
                
                /* Original Logic:
                if (!authenticationService.isLicenseeKeyAvailable()) {
                    _uiState.value = LoginUiState.NavigateToSettings
                } else {
                    _uiState.value = LoginUiState.Error(AppSettings.loginErrorMessage ?: "Invalid username or password")
                }
                */
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    object NavigateToSettings : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}