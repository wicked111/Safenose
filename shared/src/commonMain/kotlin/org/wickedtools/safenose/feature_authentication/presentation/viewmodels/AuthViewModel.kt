package org.wickedtools.safenose.feature_authentication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.wickedtools.safenose.core.domain.model.User
import org.wickedtools.safenose.feature_authentication.domain.usecases.GetCurrentUserUseCase
import org.wickedtools.safenose.feature_authentication.domain.usecases.LoginUseCase
import org.wickedtools.safenose.feature_authentication.domain.usecases.SignUpUseCase

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkExistingSession()
    }

    private fun checkExistingSession() {
        viewModelScope.launch {
            try {
                val user = getCurrentUserUseCase()
                _uiState.update { it.copy(currentUser = user, isSessionChecked = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSessionChecked = true) }
            }
        }
    }

    fun onLoginClicked(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(error = "Email and password cannot be empty") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val user = loginUseCase(email, password)
                _uiState.update { it.copy(isLoading = false, currentUser = user) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Login failed") }
            }
        }
    }

    fun onSignUpClicked(email: String, password: String, username: String, city: String) {
        if (email.isBlank() || password.isBlank() || username.isBlank() || city.isBlank()) {
            _uiState.update { it.copy(error = "All fields are required") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val user = signUpUseCase(email, password, username, city)
                _uiState.update { it.copy(isLoading = false, currentUser = user) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Sign up failed") }
            }
        }
    }

    fun onLogoutClicked() {
        _uiState.update { it.copy(currentUser = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSessionChecked: Boolean = false,
    val currentUser: User? = null,
    val error: String? = null
)
