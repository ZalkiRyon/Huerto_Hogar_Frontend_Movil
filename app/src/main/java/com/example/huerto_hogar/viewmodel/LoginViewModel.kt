package com.example.huerto_hogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.manager.UserManagerViewModel
import com.example.huerto_hogar.model.LoginResult
import com.example.huerto_hogar.model.LoginUser
import com.example.huerto_hogar.repository.UserRepository
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel refactorizado para Login con integración de API
 * 
 * Usa UserRepository para autenticar usuarios contra el backend
 * Mantiene compatibilidad con UserManagerViewModel para gestión de sesión
 */
class LoginViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    lateinit var userManager: UserManagerViewModel

    private val _uiState = MutableStateFlow(LoginUser())
    val uiState: StateFlow<LoginUser> = _uiState.asStateFlow()

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

        _uiState.update { it.copy(email = email, errors = it.errors.copy(emailError = error)) }
    }

    private fun isValidDuocEmail(email: String): Boolean {
        val lowerEmail = email.lowercase().trim()
        return lowerEmail.endsWith("@duocuc.cl") || lowerEmail.endsWith("@profesor.duoc.cl")
    }

    fun onChangePassword(password: String) {
        _uiState.update { it.copy(password = password, errors = it.errors.copy(passwordError = null)) }
    }

    fun clearLoginResultEvent() {
        _uiState.update { it.copy(loginResultEvent = null) }
    }

    /**
     * Inicia sesión usando el repositorio (API)
     * 
     * Realiza validaciones locales y luego llama al backend
     */
    fun onClickLogin() {
        val currentState = _uiState.value

        var emailError: String? = null
        var passwordError: String? = null

        // Validación de email
        if (currentState.email.isBlank()) {
            emailError = "El correo no puede estar vacío"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            emailError = "El formato del correo es incorrecto"
        } else if (!isValidDuocEmail(currentState.email)) {
            emailError = "Solo se aceptan correos @duocuc.cl o @profesor.duoc.cl"
        }

        // Validación de contraseña
        if (currentState.password.isBlank()) {
            passwordError = "La contraseña no puede estar vacía"
        }

        val hasLocalErrors = emailError != null || passwordError != null

        if (hasLocalErrors) {
            _uiState.update {
                it.copy(errors = it.errors.copy(emailError = emailError, passwordError = passwordError))
            }
            return
        }

        // Iniciar proceso de login con API
        _uiState.update { 
            it.copy(
                isLoading = true, 
                errors = it.errors.copy(emailError = null, passwordError = null)
            ) 
        }

        viewModelScope.launch {
            repository.login(currentState.email, currentState.password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Ya establecimos isLoading = true arriba
                    }
                    is Resource.Success -> {
                        resource.data?.let { loginResponse ->
                            // Guardar usuario y token en UserManager
                            userManager.setCurrentUser(loginResponse.user)
                            userManager.saveAuthToken(loginResponse.token)
                            
                            _uiState.update {
                                it.copy(
                                    loginResultEvent = LoginResult.SUCCESS,
                                    isLoading = false,
                                    loggedInUser = loginResponse.user
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                loginResultEvent = LoginResult.INVALID_CREDENTIALS,
                                isLoading = false,
                                errors = it.errors.copy(
                                    passwordError = resource.message ?: "Error al iniciar sesión"
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}