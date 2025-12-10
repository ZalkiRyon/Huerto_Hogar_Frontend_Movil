package com.example.huerto_hogar.model.uistate

import com.example.huerto_hogar.model.User

data class RegisterUserUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val run: String = "",
    val region: String = "",
    val comuna: String = "",
    val telefono: String = "",
    val direccion: String = "",

    // Estados de UI
    val isLoading: Boolean = false,
    val registrationSuccess: Boolean = false,
    val error: String? = null,
    val registeredUser: User? = null,

    // Errores de validación de campo
    val errors: RegistrationErrors = RegistrationErrors()
)

data class RegistrationErrors(
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val nameError: String? = null,
    val lastnameError: String? = null,
    val runErrors : String? = null,
    val regionErrors : String? = null,
    val comunaErrors : String? = null,
    val phoneError: String? = null,
    val addressError: String? = null,
)