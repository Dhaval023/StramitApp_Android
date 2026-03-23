package com.example.stramitapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.services.AuthenticationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ForgotPasswordUiState {
    object Idle    : ForgotPasswordUiState()
    object Loading : ForgotPasswordUiState()
    data class Success(val message: String) : ForgotPasswordUiState()
    data class Error  (val message: String) : ForgotPasswordUiState()
}

class ForgotPasswordViewModel(application: Application) : AndroidViewModel(application) {

    private val authService = AuthenticationService(application)

    val usernameText = MutableStateFlow("")

    private val _uiState = MutableStateFlow<ForgotPasswordUiState>(ForgotPasswordUiState.Idle)
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState

    fun onRetrievePasswordClicked() {
        val username = usernameText.value.trim()

        if (username.isEmpty()) {
            _uiState.value = ForgotPasswordUiState.Error("Please enter a Username to proceed.")
            return
        }

        viewModelScope.launch {
            _uiState.value = ForgotPasswordUiState.Loading

            if (!authService.isLicenseeKeyAvailable()) {
                _uiState.value = ForgotPasswordUiState.Error(
                    "Please configure your licensee settings in the Setting page."
                )
                return@launch
            }

            val success = authService.forgotPassword(username)

            _uiState.value = if (success) {
                ForgotPasswordUiState.Success(
                    "An email has been sent successfully to your email id with a link to set your new password."
                )
            } else {
                ForgotPasswordUiState.Error(
                    "An error occurred while trying to retrieve your password. Please try again."
                )
            }
        }
    }
    fun resetState() {
        _uiState.value = ForgotPasswordUiState.Idle
    }
}