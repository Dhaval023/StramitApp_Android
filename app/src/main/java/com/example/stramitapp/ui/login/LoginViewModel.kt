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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val authService = AuthenticationService(application)

    // C#: _isRememberCredentials loaded from storage
    private var isRememberCredentials: Boolean =
        StorageKeys.getRememberCredentials(application)

    // C#: _isLoginOnline = true (hardcoded)
    private var isLoginOnline: Boolean = true

    private var lastUsername = ""
    private var lastPassword = ""

    // ─── C# LoginEvent() ──────────────────────────────────────────────
    fun login(username: String, password: String) {
        // C#: if (!string.IsNullOrEmpty(UsernameText) && !string.IsNullOrEmpty(PasswordText))
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.ShowDialog(
                title   = "LOGIN FAILED!",
                message = "Please enter Username and Password to proceed."
            )
            return
        }

        // C#: if (!_service.IsLicenseeKeyAvailable()) 
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

        // C#: await Login(UsernameText, PasswordText, IsRememberCredentials, IsLoginOnline, false)
        performLogin(username, password, forceLogin = false)
    }

    // ─── C# Force Login callback ──────────────────────────────────────
    fun forceLogin() {
        performLogin(lastUsername, lastPassword, forceLogin = true)
    }

    // ─── C# private Login() ───────────────────────────────────────────
    private fun performLogin(
        username: String,
        password: String,
        forceLogin: Boolean
    ) {
        viewModelScope.launch {
            // C#: IsBusy = true
            _uiState.value = LoginUiState.Loading

            try {
                val statusCode: Int = withContext(Dispatchers.IO) {
                    if (isLoginOnline) {
                        // C#: _service.LoginOnline(...)
                        authService.loginOnline(
                            username              = username,
                            password              = password,
                            isRememberCredentials = isRememberCredentials,
                            isForceLogin          = forceLogin
                        )
                    } else {
                        // C#: _service.LoginOffline(...)
                        authService.loginOffline(
                            username              = username,
                            password              = password,
                            isRememberCredentials = isRememberCredentials
                        )
                    }
                }

                Log.d("LoginViewModel", "Login statusCode: $statusCode")

                when (statusCode) {
                    // C#: if (isAuthenticated == 1) → save prefs + navigate
                    1 -> {
                        StorageKeys.saveRememberCredentials(
                            getApplication<Application>(), isRememberCredentials)
                        StorageKeys.saveLoginOnline(
                            getApplication<Application>(), isLoginOnline)
                        _uiState.value = LoginUiState.Success
                    }

                    // C#: else if (isAuthenticated == 3) → show force login dialog
                    3 -> {
                        _uiState.value = LoginUiState.ForceLoginRequired(
                            message = authService.loginErrorMessage.ifBlank {
                                "User already logged in on some other device."
                            }
                        )
                    }

                    // C#: else → ShowMessageDialog("LOGIN FAILED!", AppSettings.LoginErrorMessage)
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

    // ─── C# RememberLogin() ───────────────────────────────────────────
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
        }
    }

    fun toggleRememberCredentials() { isRememberCredentials = !isRememberCredentials }
    fun toggleLoginOnline() { isLoginOnline = !isLoginOnline }

    fun resetState() { _uiState.value = LoginUiState.Idle }

    private fun isNetworkAvailable(): Boolean {
        val cm = getApplication<Application>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps    = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

// ✅ Sealed class matches ALL C# dialog/navigation cases exactly
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    object NoLicenseKey : LoginUiState()

    // C#: ShowForceLoginDialog — carries server message
    data class ForceLoginRequired(val message: String) : LoginUiState()

    // C#: ShowMessageDialog — carries title + message
    data class ShowDialog(val title: String, val message: String) : LoginUiState()
}