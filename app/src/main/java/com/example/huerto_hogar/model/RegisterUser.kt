package com.example.huerto_hogar.model

enum class RegistrationResult {
    SUCCESS,
    EMAIL_ALREADY_EXISTS,
    ERROR
}

// CLASS FOR STATE FORM
data class RegisterUser(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val name: String = "",
    val lastname: String = "",
    val phone: String = "",
    val address: String = "",
    val registrationResultEvent: RegistrationResult? = null,
    val isLoading: Boolean = false,
    val errors: RegisterUserErrors = RegisterUserErrors(),

    val registeredUser: User? = null
)

// CLASS MANAGE ERROR STATE FORM
data class RegisterUserErrors(
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val nameError: String? = null,
    val lastnameError: String? = null,
    val addressError: String? = null,
    val phoneError: String? = null,
)
