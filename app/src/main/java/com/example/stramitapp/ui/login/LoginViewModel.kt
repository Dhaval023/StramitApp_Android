package com.example.stramitapp.ui.login

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.models.Constants.StorageKeys
import com.example.stramitapp.services.AuthenticationService
import com.example.stramitapp.services.AuthenticatedUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _authenticatedUser = MutableStateFlow<AuthenticatedUser?>(null)
    val authenticatedUser: StateFlow<AuthenticatedUser?> = _authenticatedUser.asStateFlow()

    private val authService = AuthenticationService(application)

    private var isRememberCredentials: Boolean =
        StorageKeys.getRememberCredentials(application)

    private var isLoginOnline: Boolean = true

    private var lastUsername = ""
    private var lastPassword = ""

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.ShowDialog(
                title   = "LOGIN FAILED!",
                message = "Please enter Username and Password to proceed."
            )
            return
        }

        if (!authService.isLicenseeKeyAvailable()) {
            _uiState.value = LoginUiState.NoLicenseKey
            return
        }

        if (!isNetworkAvailable()) {
            _uiState.value = LoginUiState.ShowDialog(
                title   = "LOGIN FAILED!",
                message = "No internet connection."
            )
            return
        }

        lastUsername = username
        lastPassword = password

        performLogin(username, password, forceLogin = false)
    }

    fun forceLogin() {
        performLogin(lastUsername, lastPassword, forceLogin = true)
    }

    private fun performLogin(
        username: String,
        password: String,
        forceLogin: Boolean
    ) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            try {
                val statusCode: Int = withContext(Dispatchers.IO) {
                    if (isLoginOnline) {
                        authService.loginOnline(
                            username              = username,
                            password              = password,
                            isRememberCredentials = isRememberCredentials,
                            isForceLogin          = forceLogin
                        )
                    } else {
                        authService.loginOffline(
                            username              = username,
                            password              = password,
                            isRememberCredentials = isRememberCredentials
                        )
                    }
                }

                Log.d("LoginViewModel", "Login statusCode: $statusCode")

                when (statusCode) {
                    1 -> {
                        _authenticatedUser.value = authService.authenticatedUser

                        StorageKeys.saveRememberCredentials(
                            getApplication<Application>(), isRememberCredentials)
                        StorageKeys.saveLoginOnline(
                            getApplication<Application>(), isLoginOnline)
                        _uiState.value = LoginUiState.Success
                    }

                    3 -> {
                        _uiState.value = LoginUiState.ForceLoginRequired(
                            message = authService.loginErrorMessage.ifBlank {
                                "User already logged in on some other device."
                            }
                        )
                    }

                    else -> {
                        _uiState.value = LoginUiState.ShowDialog(
                            title   = "LOGIN FAILED!",
                            message = authService.loginErrorMessage.ifBlank {
                                "Login failed. Please check your credentials."
                            }
                        )
                    }
                }

            } catch (e: java.net.UnknownHostException) {
                _uiState.value = LoginUiState.ShowDialog(
                    title   = "LOGIN FAILED!",
                    message = "Cannot reach server. Check server URL."
                )
            } catch (e: java.net.SocketTimeoutException) {
                _uiState.value = LoginUiState.ShowDialog(
                    title   = "LOGIN FAILED!",
                    message = "Request timed out. Please try again."
                )
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error: ${e.javaClass.simpleName}", e)
                _uiState.value = LoginUiState.ShowDialog(
                    title   = "LOGIN FAILED!",
                    message = e.message ?: "An unknown error occurred"
                )
            }
        }
    }

    fun rememberLogin() {
        val username = StorageKeys.getUsername(getApplication<Application>())
        val password = StorageKeys.getPassword(getApplication<Application>())

        if (username.isBlank() || password.isBlank()) return

        lastUsername = username
        lastPassword = password
        performLogin(username, password, forceLogin = false)
    }

    fun logout() {
        viewModelScope.launch {
            authService.logout()
            _authenticatedUser.value = null
        }
    }

    fun toggleRememberCredentials() { isRememberCredentials = !isRememberCredentials }
    fun toggleLoginOnline()         { isLoginOnline = !isLoginOnline }
    fun resetState()                { _uiState.value = LoginUiState.Idle }

    private fun isNetworkAvailable(): Boolean {
        val cm = getApplication<Application>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps    = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

sealed class LoginUiState {
    object Idle    : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    object NoLicenseKey : LoginUiState()
    data class ForceLoginRequired(val message: String) : LoginUiState()
    data class ShowDialog(val title: String, val message: String) : LoginUiState()
}