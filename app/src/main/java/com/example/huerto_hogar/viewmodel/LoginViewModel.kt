package com.example.huerto_hogar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.huerto_hogar.manager.UserManagerViewModel
import com.example.huerto_hogar.model.LoginResult
import com.example.huerto_hogar.model.LoginUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel() : ViewModel() {

    lateinit var userManager: UserManagerViewModel

    private val _uiState = MutableStateFlow(LoginUser())
    val uiState: StateFlow<LoginUser> = _uiState.asStateFlow()

    fun onChangeEmail(email: String) {
        _uiState.update { it.copy(email = email, errors = it.errors.copy(emailError = null)) }
    }

    fun onChangePassword(password: String) {
        _uiState.update { it.copy(password = password, errors = it.errors.copy(passwordError = null)) }
    }

    fun clearLoginResultEvent() {
        _uiState.update { it.copy(loginResultEvent = null) }
    }

    fun onClickLogin() {
        val currentState = _uiState.value


        val emailError = if (currentState.email.isBlank()) "El correo no puede estar vacío" else null
        val passwordError = if (currentState.password.isBlank()) "La contraseña no puede estar vacía" else null

        val hasLocalErrors = emailError != null || passwordError != null

        if (hasLocalErrors) {
            _uiState.update {
                it.copy(errors = it.errors.copy(emailError = emailError, passwordError = passwordError))
            }
            return
        }


        _uiState.update { it.copy(isLoading = true, errors = it.errors.copy(emailError = null, passwordError = null)) }


        val foundUser = userManager.findUserByCredentials(
            email = currentState.email,
            password = currentState.password
        )


        val result = if (foundUser != null) {
            userManager.setCurrentUser(foundUser)
            LoginResult.SUCCESS
        } else {
            LoginResult.INVALID_CREDENTIALS
        }


        _uiState.update {
            it.copy(
                loginResultEvent = result,
                isLoading = false,
                loggedInUser = foundUser
            )
        }
    }
}