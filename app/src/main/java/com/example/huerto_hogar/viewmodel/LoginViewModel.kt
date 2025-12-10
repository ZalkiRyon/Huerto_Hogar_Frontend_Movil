package com.example.huerto_hogar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.model.UserLoginRequest
import com.example.huerto_hogar.model.uistate.LoginUiState
import com.example.huerto_hogar.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository = UserRepository(),
    private val userViewModel: UserViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onChangeEmail(email: String) {
        var error: String? = null
        val trimmedEmail = email.trim()

        if (trimmedEmail.isNotEmpty()) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
                error = "El formato del correo es incorrecto"
            } else if (!isValidDuocEmail(trimmedEmail)) {
                error = "Solo se aceptan correos @duocuc.cl o @profesor.duoc.cl"
            }
        }

        _uiState.update {
            it.copy(
                email = email,
                loginErrors = it.loginErrors.copy(emailError = error)
            )
        }
    }

    private fun isValidDuocEmail(email: String): Boolean {
        val lowerEmail = email.lowercase().trim()
        return lowerEmail.endsWith("@duocuc.cl") || lowerEmail.endsWith("@profesor.duoc.cl")
    }

    fun onChangePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                loginErrors = it.loginErrors.copy(passwordError = null)
            )
        }
    }

    fun onClickLogin() {
        val currentState = _uiState.value

        onChangeEmail(currentState.email)
        onChangePassword(currentState.password)

        val validatedState = _uiState.value
        val hasLocalErrors =
            validatedState.loginErrors.emailError != null || validatedState.loginErrors.passwordError != null

        if (hasLocalErrors) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val request = UserLoginRequest(
                    email = validatedState.email,
                    password = validatedState.password
                )

                val response = repository.loginUser(request)

                if (response.token != null && response.user != null) {
                    response.user.let { userObject ->
                        userViewModel.setCurrentUser(userObject)
                    }

                    response.token.let { token ->
                        userViewModel.saveAuthToken(token)
                    }
                    Log.d("LOGINVIEWMODEL", "TRY ${response}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginSuccess = true,
                            loggedInUser = response.user,
                            token = response.token,
                            error = null
                        )
                    }
                } else {
                    val errorMessage = response.message.takeIf { it.isNotBlank() }
                        ?: "Credenciales inválidas. Intente de nuevo."

                    Log.d("LOGINVIEWMODEL", "TRY ELSE ${response}")

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginSuccess = false,
                            error = errorMessage
                        )
                    }
                }
            } catch (e: Exception) {
                val finalErrorMessage = "Error de conexión: ${e.message}"
                Log.d("LOGINVIEWMODEL", "CATCH ${e.message}")

                val errorMessage = when {
                    e.message?.contains("400", ignoreCase = true) == true ->
                        "Faltan datos obligatorios (email y contraseña)."

                    e.message?.contains("401", ignoreCase = true) == true ->
                        "Credenciales incorrectas. Revise su email y contraseña."

                    e.message?.contains("403", ignoreCase = true) == true ->
                        "Usuario inactivo"

                    e.message?.contains("404", ignoreCase = true) == true ->
                        "Usuario no encontrado. Verifique su correo electrónico."

                    e.message?.contains("500", ignoreCase = true) == true ->
                        "Error interno del servidor. Intente más tarde."

                    else -> "Error de conexión inesperado: ${e.message}"
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginSuccess = false,
                        error = errorMessage
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetUiState() {
        _uiState.update { LoginUiState() }
    }

}

